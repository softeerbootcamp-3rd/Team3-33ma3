package softeer.be33ma3.dto.response;

import lombok.Builder;
import softeer.be33ma3.domain.Post;

import java.util.Arrays;
import java.util.List;

@Builder
public class PostDetailDto {
    private Long postId;
    private String carType;
    private String modelName;
    private int deadLine;
    private String regionName;
    private String contents;
    private List<String> repairList;
    private List<String> tuneUpList;

    // Post Entity -> PostDetailDto 변환
    public static PostDetailDto fromEntity(Post post) {
        List<String> repairList = stringCommaParsing(post.getRepairService());
        List<String> tuneUpList = stringCommaParsing(post.getTuneUpService());
        return PostDetailDto.builder()
                .postId(post.getPostId())
                .carType(post.getCarType())
                .modelName(post.getModelName())
                .deadLine(post.getDeadline())
                .regionName(post.getRegion().getRegionName())
                .contents(post.getContents())
                .repairList(repairList)
                .tuneUpList(tuneUpList).build();
    }

    // 구분자 콤마로 문자열 파싱 후 각각의 토큰에서 공백 제거 후 리스트 반환
    private static List<String> stringCommaParsing(String inputString) {
        return Arrays.stream(inputString.split(","))
                .map(String::strip)
                .toList();
    }
}
