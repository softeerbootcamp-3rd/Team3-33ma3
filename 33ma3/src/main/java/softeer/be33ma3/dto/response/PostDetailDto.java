package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import softeer.be33ma3.domain.Image;
import softeer.be33ma3.domain.Post;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Builder
@Getter
@Schema(description = "게시글 상세보기 응답 DTO")
public class PostDetailDto {
    @Schema(description = "게시글 아이디", example = "1")
    private Long postId;

    @Schema(description = "작성자 아이디", example = "1")
    private Long writerId;

    @Schema(description = "차종", example = "승용차")
    private String carType;

    @Schema(description = "모델명", example = "제네시스")
    private String modelName;

    @Schema(description = "남은 기한", example = "3")
    private int dDay;

    @Schema(description = "당일 남은 시간 (초)", example = "12345")
    private int remainTime;

    @Schema(description = "지역 명", example = "강남구")
    private String regionName;

    @Schema(description = "내용", example = "운전하다가 기스를 내버렸어요ㅠㅠ")
    private String contents;

    @Schema(description = "수리 서비스 리스트", example = "[깨짐, 기스]")
    private List<String> repairList;

    @Schema(description = "정비 서비스 리스트", example = "[타이어 교체, 오일 교체]")
    private List<String> tuneUpList;

    @Schema(description = "이미지 url 리스트", example = "[aaa.png, bbb.png]")
    private List<String> imageList;

    // Post Entity -> PostDetailDto 변환
    public static PostDetailDto fromEntity(Post post) {
        List<String> repairList = stringCommaParsing(post.getRepairService());
        List<String> tuneUpList = stringCommaParsing(post.getTuneUpService());
        List<String> imageList = post.getImages().stream().map(Image::getLink).toList();
        Duration duration = calculateDuration(post);
        int dDay = -1;
        int remainTime = 0;
        if(!post.isDone() && !duration.isNegative())        // 아직 마감 시간 전
            dDay = (int)duration.toDays();
        if(dDay == 0)
            remainTime = calculateRemainTime(duration);

        return PostDetailDto.builder()
                .postId(post.getPostId())
                .writerId(post.getMember().getMemberId())
                .carType(post.getCarType())
                .modelName(post.getModelName())
                .dDay(dDay)
                .remainTime(remainTime)
                .regionName(post.getRegion().getRegionName())
                .contents(post.getContents())
                .repairList(repairList)
                .tuneUpList(tuneUpList)
                .imageList(imageList).build();
    }

    // 구분자 콤마로 문자열 파싱 후 각각의 토큰에서 공백 제거 후 리스트 반환
    public static List<String> stringCommaParsing(String inputString) {
        if(inputString.isEmpty())
            return List.of();
        return Arrays.stream(inputString.split(","))
                .map(String::strip)
                .toList();
    }

    public static Duration calculateDuration(Post post) {
        LocalDateTime endTime = post.getCreateTime().plusDays(post.getDeadline());
        endTime = endTime.withHour(23).withMinute(59).withSecond(59);
        return Duration.between(LocalDateTime.now(), endTime);
    }

    // 마감 시간까지 남은 시간을 초 단위로 반환
    public static int calculateRemainTime(Duration duration) {
        return (int) duration.toSeconds();
    }
}
