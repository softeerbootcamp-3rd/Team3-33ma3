package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import softeer.be33ma3.domain.*;
import softeer.be33ma3.dto.request.PostCreateDto;
import softeer.be33ma3.dto.response.ImageListDto;
import softeer.be33ma3.dto.response.OfferDetailDto;
import softeer.be33ma3.dto.response.PostDetailDto;
import softeer.be33ma3.exception.UnauthorizedException;
import softeer.be33ma3.repository.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private static final int CLIENT_TYPE = 1;

    private final OfferRepository offerRepository;
    private final CenterRepository centerRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final PostPerCenterRepository postPerCenterRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    @Transactional
    public Long createPost(Member currentMember, PostCreateDto postCreateDto, List<MultipartFile> multipartFiles) {
        //회원이랑 지역 찾기
        Member member = memberRepository.findById(currentMember.getMemberId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
        if(member.getMemberType() != CLIENT_TYPE){
            throw new UnauthorizedException("센터는 글 작성이 불가능합니다.");
        }
        Region region = getRegion(postCreateDto.getLocation());

        //이미지 저장
        ImageListDto imageListDto = imageService.saveImage(multipartFiles);

        //게시글 저장
        Post post = Post.createPost(postCreateDto, region, member);
        Post savedPost = postRepository.save(post);

        //정비소랑 게시물 매핑하기
        centerAndPostMapping(postCreateDto, savedPost);

        //이미지랑 게시물 매핑하기
        List<Image> images = imageRepository.findAllById(imageListDto.getImageIds());
        images.forEach(image -> image.setPost(savedPost));

        return savedPost.getPostId();
    }


    @Transactional
    public void editPost(Member member, Long postId, PostCreateDto postCreateDto) {
        Post post = validPostAndMember(member, postId);

        Region region = getRegion(postCreateDto.getLocation());
        post.editPost(postCreateDto, region);
    }
    @Transactional
    public void deletePost(Member member, Long postId) {
        Post post = validPostAndMember(member, postId);
        //이미지 삭제
        List<Image> images = post.getImages();
        imageService.deleteImage(images);   //S3에서 이미지 삭제
        imageRepository.deleteAll(images);  //db에 저장된 이미지 삭제
        //게시글 삭제
        postRepository.delete(post);
    }

    // 게시글 세부사항 반환 (로그인 하지 않아도 확인 가능)
    public List<Object> showPost(Long postId, Member member) {
        // 1. 게시글 존재 유무 판단
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));
        // 2. 게시글 세부 사항 가져오기
        PostDetailDto postDetailDto = PostDetailDto.fromEntity(post);
        // 3. 경매가 완료되었거나 글 작성자의 접근일 경우
        if(post.isDone() || (member!=null && post.getMember().equals(member))) {
            List<Offer> offerList = offerRepository.findByPost_PostId(postId);
            List<OfferDetailDto> offerDetailList = OfferDetailDto.fromEntityList(offerList);
            return List.of(postDetailDto, offerDetailList);
        }
        // 4. 경매가 진행 중이고 작성자가 아닌 유저의 접근일 경우
        // 해당 게시글의 견적 모두 가져오기
        List<Offer> offerList = offerRepository.findByPost_PostId(postId);
        double avgPrice = OfferService.calculateAvgPrice(offerList);
        // 견적을 작성한 이력이 있는 서비스 센터의 접근일 경우 작성한 견적 가져오기
        OfferDetailDto offerDetailDto = getCenterOffer(postId, member);
        return (offerDetailDto == null) ? List.of(postDetailDto, avgPrice) : List.of(postDetailDto, avgPrice, offerDetailDto);
    }

    // 멤버 정보를 이용하여 견적을 작성한 이력이 있는 서비스 센터일 경우 작성한 견적 반환
    // 해당사항 없을 경우 null 반환
    private OfferDetailDto getCenterOffer(Long postId, Member member) {
        if(member == null || member.getMemberType() == MemberService.CLIENT_TYPE)
            return null;
        // 센터 엔티티 찾기
        Center center = centerRepository.findByMember_MemberId(member.getMemberId())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 서비스 센터"));
        // 해당 게시글에 해당 센터가 작성한 견적 찾기
        Optional<Offer> offer = offerRepository.findByPost_PostIdAndCenter_CenterId(postId, center.getCenterId());
        return (offer.isEmpty() ? null : OfferDetailDto.fromEntity(offer.get()));
    }

    private Region getRegion(String location) {
        // 정규 표현식을 사용하여 "구"로 끝나는 문자열 찾기
        Pattern pattern = Pattern.compile("\\b([^\\s]+구)\\b");
        Matcher matcher = pattern.matcher(location);

        if (matcher.find()) {
            return regionRepository.findByRegionName(matcher.group()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구"));
        }

        throw new IllegalArgumentException("주소에서 구를 찾을 수 없음");
    }

    private void centerAndPostMapping(PostCreateDto postCreateDto, Post savedPost) {
        List<Center> centers = centerRepository.findAllById(postCreateDto.getCenters());

        List<PostPerCenter> postPerCenters = centers.stream()
                .map(center -> new PostPerCenter(center, savedPost))
                .collect(Collectors.toList());

        postPerCenterRepository.saveAll(postPerCenters);
    }

    private Post validPostAndMember(Member member, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));
        Member findMember = memberRepository.findById(member.getMemberId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        if (!post.getMember().equals(findMember)) {
            throw new UnauthorizedException("작성자만 가능합니다.");
        }

        if (!offerRepository.findByPost_PostId(postId).isEmpty()) { //댓글이 있는 경우(경매 시작 후)
            throw new IllegalArgumentException("경매 시작 전에만 가능합니다.");
        }

        return post;
    }
}
