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

    private final OfferRepository offerRepository;
    private final CenterRepository centerRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final PostPerCenterRepository postPerCenterRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
  
    @Transactional
    public void createPost(PostCreateDto postCreateDto, List<MultipartFile> multipartFiles) {
        //이미지 저장
        ImageListDto imageListDto = imageService.saveImage(multipartFiles);
        String location = postCreateDto.getLocation();

        //회원이랑 지역 찾기
        Member member = memberRepository.findById(postCreateDto.getMemberId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
        Region region = regionRepository.findByRegionName(getRegion(location)).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구"));

        Post post = Post.createPost(postCreateDto, region, member);
        Post savedPost = postRepository.save(post);

        //정비소랑 게시물 매핑하기
        centerAndPostMapping(postCreateDto, savedPost);

        //이미지랑 게시물 매핑하기
        List<Image> images = imageRepository.findAllById(imageListDto.getImageIds());
        images.forEach(image -> image.setPost(savedPost));
    }

    private void centerAndPostMapping(PostCreateDto postCreateDto, Post savedPost) {
        List<Center> centers = centerRepository.findAllById(postCreateDto.getCenters());

        List<PostPerCenter> postPerCenters = centers.stream()
                .map(center -> new PostPerCenter(center, savedPost))
                .collect(Collectors.toList());

        postPerCenterRepository.saveAll(postPerCenters);
    }

    private String getRegion(String location) {
        // 정규 표현식을 사용하여 "구"로 끝나는 문자열 찾기
        Pattern pattern = Pattern.compile("\\b([^\\s]+구)\\b");
        Matcher matcher = pattern.matcher(location);

        if (matcher.find()) {
            return matcher.group();
        }

        throw new IllegalArgumentException("주소에서 구를 찾을 수 없음");
    }

    // 게시글 세부사항 반환 (로그인 하지 않아도 확인 가능)
    public List<Object> getPost(Long postId) {
        // 1. 게시글 존재 유무 판단
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));
        // 2. 게시글 세부 사항 가져오기
        PostDetailDto postDetailDto = PostDetailDto.fromEntity(post);
        // 3. TODO: 접근자 정보 가져오기
        Optional<Member> member = Optional.empty();
        // 3-1. 경매가 완료되었거나 글 작성자의 접근일 경우
        if(post.isDone() ||
                member.isPresent() && post.getMember().getMemberId() == member.get().getMemberId()) {
            List<Offer> offerList = offerRepository.findByPost_PostId(postId);
            List<OfferDetailDto> offerDetailList = offerList.stream()
                    .map(OfferDetailDto::fromEntity)
                    .toList();
            return List.of(postDetailDto, offerDetailList);
        }
        // 3-2. 경매가 진행 중이고 작성자가 아닌 유저의 접근일 경우
        double priceAvg = priceAvgOfPost(postId);
        // 견적을 작성한 이력이 있는 서비스 센터의 접근일 경우 작성한 견적 가져오기
        OfferDetailDto offerDetailDto = getCenterOffer(postId, member.get());
        return List.of(postDetailDto, priceAvg, offerDetailDto);
    }

    // 해당 게시글의 평균 견적 제시 가격 반환
    private double priceAvgOfPost(Long postId) {
        // 해당 게시글의 견적 모두 가져오기
        List<Offer> offerList = offerRepository.findByPost_PostId(postId);

        // 제시 가격의 합계, 개수 구하기
        IntSummaryStatistics stats = offerList.stream()
                .collect(Collectors.summarizingInt(Offer::getPrice));

        if(stats.getCount() == 0)
            throw new ArithmeticException("견적 제시 댓글이 없습니다.");
        return stats.getAverage();
    }

    // 멤버 정보를 이용하여 견적을 작성한 이력이 있는 서비스 센터일 경우 작성한 견적 반환
    // 해당사항 없을 경우 null 반환
    private OfferDetailDto getCenterOffer(Long postId, Member member) {
        if(member.getMemberType() == 1)
            return null;
        // 센터 엔티티 찾기
        Center center = centerRepository.findByMember_MemberId(member.getMemberId())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 서비스 센터"));
        // 해당 게시글에 해당 센터가 작성한 견적 찾기
        Optional<Offer> offer = offerRepository.findByCenter_CenterId(center.getCenterId());
        return (offer.isEmpty() ? null : OfferDetailDto.fromEntity(offer.get()));
    }
}
