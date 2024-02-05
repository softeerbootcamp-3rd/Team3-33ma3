package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.*;
import softeer.be33ma3.dto.request.PostCreateDto;
import softeer.be33ma3.repository.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
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
}
