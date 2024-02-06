package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.*;
import softeer.be33ma3.dto.request.PostCreateDto;
import softeer.be33ma3.repository.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.IntSummaryStatistics;
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
  
    @Transactional
    public void createPost(PostCreateDto postCreateDto) {
        long memberId = postCreateDto.getMemberId();
        String location = postCreateDto.getLocation();

        //회원이랑 지역 찾기
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
        Region region = regionRepository.findByRegionName(getRegion(location)).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구"));

        Post post = Post.createPost(postCreateDto, region, member);
        Post savedPost = postRepository.save(post);

        //정비소랑 게시물 매핑하기
        centerAndPostMapping(postCreateDto, savedPost);

        //이미지랑 게시물 매핑하기
        List<Image> images = imageRepository.findAllById(postCreateDto.getImages());
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
  
    // 해당 게시글의 평균 견적 제시 가격 반환
    double priceAvgOfPost(Long postId) {
        // 해당 게시글의 견적 모두 가져오기
        List<Offer> offerList = offerRepository.findByPost_PostId(postId);

        // 제시 가격의 합계, 개수 구하기
        IntSummaryStatistics stats = offerList.stream()
                .collect(Collectors.summarizingInt(Offer::getPrice));

        if(stats.getCount() == 0)
            throw new ArithmeticException("견적 제시 댓글이 없습니다.");
        return stats.getAverage();
    }
}
