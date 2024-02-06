package softeer.be33ma3.dto.response;

import lombok.Builder;
import softeer.be33ma3.domain.Image;
import softeer.be33ma3.domain.Post;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Builder
public class PostDetailDto {
    private Long postId;
    private String carType;
    private String modelName;
    private int dDay;
    private LocalDateTime serverTime;
    private String regionName;
    private String contents;
    private List<String> repairList;
    private List<String> tuneUpList;
    private List<String> imageList;

    // Post Entity -> PostDetailDto 변환
    public static PostDetailDto fromEntity(Post post) {
        List<String> repairList = stringCommaParsing(post.getRepairService());
        List<String> tuneUpList = stringCommaParsing(post.getTuneUpService());
        List<String> imageList = post.getImages().stream().map(Image::getLink).toList();
        int betweenTime = (int)ChronoUnit.DAYS.between(LocalDateTime.now(), post.getCreateTime());
        return PostDetailDto.builder()
                .postId(post.getPostId())
                .carType(post.getCarType())
                .modelName(post.getModelName())
                .dDay(post.getDeadline() - betweenTime)
                .serverTime(LocalDateTime.now())
                .regionName(post.getRegion().getRegionName())
                .contents(post.getContents())
                .repairList(repairList)
                .tuneUpList(tuneUpList)
                .imageList(imageList).build();
    }

    // 구분자 콤마로 문자열 파싱 후 각각의 토큰에서 공백 제거 후 리스트 반환
    private static List<String> stringCommaParsing(String inputString) {
        return Arrays.stream(inputString.split(","))
                .map(String::strip)
                .toList();
    }
}
