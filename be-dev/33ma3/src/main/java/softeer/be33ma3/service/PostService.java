package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import softeer.be33ma3.domain.*;
import softeer.be33ma3.dto.request.PostCreateDto;
import softeer.be33ma3.dto.response.*;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.repository.*;
import softeer.be33ma3.repository.PostRepository;
import softeer.be33ma3.repository.ReviewRepository;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.List;

import static softeer.be33ma3.exception.ErrorCode.*;
import static softeer.be33ma3.utils.StringParser.stringCommaParsing;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final OfferRepository offerRepository;
    private final CenterRepository centerRepository;
    private final PostRepository postRepository;
    private final RegionRepository regionRepository;
    private final PostPerCenterRepository postPerCenterRepository;
    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    // 게시글 목록 조회
    public List<PostThumbnailDto> showPosts(Boolean mine, Boolean done, String region, String repair, String tuneUp, Member member) {
        List<String> regions = stringCommaParsing(region);
        List<String> repairs = stringCommaParsing(repair);
        List<String> tuneUps = stringCommaParsing(tuneUp);
        List<Long> postIds = null;
        if(member != null && member.isCenter()) {   //센터인 경우
            Center center = centerRepository.findByMember_MemberId(member.getMemberId()).orElseThrow(() -> new BusinessException(NOT_FOUND_CENTER));
            postIds = postPerCenterRepository.findPostIdsByCenterId(center.getCenterId());
        }
        Long writerId = null;
        if(Boolean.TRUE.equals(mine) && member != null && member.isClient()) {
            writerId = member.getMemberId();
        }
        List<Post> posts = postRepository.findAllByConditions(writerId, done, regions, repairs, tuneUps, postIds);
        return fromPostList(posts);
    }

    // List<Post> -> List<PostThumbnailDto>로 변환
    private List<PostThumbnailDto> fromPostList(List<Post> posts) {
        return posts.stream()
                .map(PostThumbnailDto::fromEntity).toList();
    }

    @Transactional
    public Long createPost(Member currentMember, PostCreateDto postCreateDto, List<MultipartFile> multipartFiles) {
        if(currentMember.isCenter()){   //센터인 경우 글 작성 불가능
            throw new BusinessException(POST_CREATION_DISABLED);
        }
        Region region = getRegion(postCreateDto.getLocation()); //지역 찾기

        //게시글 저장
        Post post = Post.createPost(postCreateDto, region, currentMember);
        Post savedPost = postRepository.save(post);

        //정비소랑 게시물 매핑하기
        centerAndPostMapping(postCreateDto, savedPost);

        if(multipartFiles == null){     //이미지가 없는 경우
            return savedPost.getPostId();
        }

        //이미지 저장
        List<Image> images = imageService.saveImages(multipartFiles);

        //이미지랑 게시물 매핑하기
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
    public Object showPost(Long postId, Member member) {
        // 1. 게시글 존재 유무 판단
        Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessException(NOT_FOUND_POST));
        if(member == null && !post.isDone())
            throw new BusinessException(LOGIN_REQUIRED);
        // 2. 게시글 세부 사항 가져오기
        PostDetailDto postDetailDto = PostDetailDto.fromEntity(post);
        // 3. 경매가 완료되었거나 글 작성자의 접근일 경우
        if(post.isDone() || (member!=null && post.getMember().getMemberId().equals(member.getMemberId()))) {
            List<Offer> offerList = post.getOffers();
            List<OfferDetailDto> offerDetailDtos = new ArrayList<>(
                    offerList.stream().map(offer -> {
                        Double score = reviewRepository.findAvgScoreByCenterId(offer.getCenter().getMemberId()).orElse(0.0);
                        return OfferDetailDto.fromEntity(offer, score);
                    }).toList());
            Collections.sort(offerDetailDtos);
            return new PostWithOffersDto(postDetailDto, offerDetailDtos);
        }
        // 4. 경매가 진행 중이고 작성자가 아닌 유저의 접근일 경우
        Double avgPrice = offerRepository.findAvgPriceByPostId(postId).orElse(0.0);
        PostWithAvgPriceDto postWithAvgPriceDto = new PostWithAvgPriceDto(postDetailDto, Math.round( avgPrice * 10 ) / 10.0);
        // 견적을 작성한 이력이 있는 서비스 센터의 접근일 경우 작성한 견적 가져오기
        OfferDetailDto offerDetailDto = getCenterOffer(postId, member);
        postWithAvgPriceDto.setOfferDetailDto(offerDetailDto);
        return postWithAvgPriceDto;
    }

    // 멤버 정보를 이용하여 견적을 작성한 이력이 있는 서비스 센터일 경우 작성한 견적 반환
    // 해당사항 없을 경우 null 반환
    private OfferDetailDto getCenterOffer(Long postId, Member member) {
        if(member == null || member.isClient())
            return null;
        // 해당 게시글에 해당 센터가 작성한 견적 찾기
        Optional<Offer> offer = offerRepository.findByPost_PostIdAndCenter_MemberId(postId, member.getMemberId());
        if(offer.isEmpty()) {
            return null;
        }
        Double score = reviewRepository.findAvgScoreByCenterId(offer.get().getCenter().getMemberId()).orElse(0.0);
        return OfferDetailDto.fromEntity(offer.get(), score);
    }

    private Region getRegion(String location) {
        // 정규 표현식을 사용하여 "구"로 끝나는 문자열 찾기
        Pattern pattern = Pattern.compile("\\b([^\\s]+구)\\b");
        Matcher matcher = pattern.matcher(location);

        if (matcher.find()) {
            return regionRepository.findByRegionName(matcher.group()).orElseThrow(() -> new BusinessException(NOT_FOUND_REGION));
        }

        throw new BusinessException(NO_DISTRICT_IN_ADDRESS);
    }

    private void centerAndPostMapping(PostCreateDto postCreateDto, Post savedPost) {
        List<Center> centers = centerRepository.findAllById(postCreateDto.getCenters());

        List<PostPerCenter> postPerCenters = centers.stream()
                .map(center -> new PostPerCenter(center, savedPost))
                .collect(Collectors.toList());

        postPerCenterRepository.saveAll(postPerCenters);
    }

    private Post validPostAndMember(Member member, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessException(NOT_FOUND_POST));

        if (!post.getMember().getMemberId().equals(member.getMemberId())) {
            throw new BusinessException(AUTHOR_ONLY_ACCESS);
        }
        if (!post.getOffers().isEmpty()) { //댓글이 있는 경우(경매 시작 후)
            throw new BusinessException(PRE_AUCTION_ONLY);
        }
        return post;
    }
}
