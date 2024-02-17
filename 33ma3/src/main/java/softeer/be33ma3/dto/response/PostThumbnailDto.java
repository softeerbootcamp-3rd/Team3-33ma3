package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import softeer.be33ma3.domain.Image;
import softeer.be33ma3.domain.Post;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
@Schema(description = "게시글 목록 미리보기 응답 DTO")
public class PostThumbnailDto implements Comparable<PostThumbnailDto> {
    @Schema(description = "게시글 아이디", example = "1")
    private Long postId;

    @Schema(description = "작성자 아이디", example = "1")
    private Long writerId;

    @Schema(description = "모델명", example = "제네시스")
    private String modelName;

    @Schema(description = "게시글 생성 시간 LocalDateTime", example = "2024-02-06T02:23:25.043239")
    private LocalDateTime rawCreateTime;

    @Schema(description = "게시글 생성 시간 문자열", example = "2024.02.06")
    private String createTime;

    @Schema(description = "남은 기한", example = "3")
    private int dDay;

    @Schema(description = "당일 남은 시간 (초)", example = "12345")
    private int remainTime;

    @Schema(description = "이미지 url 리스트", example = "[aaa.png, bbb.png]")
    private List<String> imageList;

    @Schema(description = "수리 서비스 리스트", example = "[깨짐, 기스]")
    private List<String> repairList;

    @Schema(description = "정비 서비스 리스트", example = "[타이어 교체, 오일 교체]")
    private List<String> tuneUpList;

    @Schema(description = "댓글 개수", example = "1")
    private int offerCount;

    // 생성 시간 기준으로 최신순 정렬
    @Override
    public int compareTo(PostThumbnailDto other) {
        if(this.rawCreateTime.isAfter(other.getRawCreateTime()))
            return -1;
        else if(this.rawCreateTime.isBefore(other.getRawCreateTime()))
            return 1;
        // createTime이 동일할 경우
        if(postId > other.getPostId())
            return -1;
        else
            return 1;
    }

    // Post Entity -> PostThumbnailDto 변환
    public static PostThumbnailDto fromEntity(Post post, int offerCount) {
        List<String> imageList = post.getImages().stream().map(Image::getLink).toList();
        List<String> repairList = PostDetailDto.stringCommaParsing(post.getRepairService());
        List<String> tuneUpList = PostDetailDto.stringCommaParsing(post.getTuneUpService());
        Duration duration = PostDetailDto.calculateDuration(post);
        int dDay = -1;
        int remainTime = 0;
        if(!post.isDone() && !duration.isNegative())        // 아직 마감 시간 전
            dDay = (int)duration.toDays();
        if(dDay == 0)
            remainTime = PostDetailDto.calculateRemainTime(duration);

        return PostThumbnailDto.builder()
                .postId(post.getPostId())
                .writerId(post.getMember().getMemberId())
                .modelName(post.getModelName())
                .rawCreateTime(post.getCreateTime())
                .createTime(createTimeFormatting(post.getCreateTime()))
                .dDay(dDay)
                .remainTime(remainTime)
                .repairList(repairList)
                .tuneUpList(tuneUpList)
                .imageList(imageList)
                .offerCount(offerCount).build();
    }

    private static String createTimeFormatting(LocalDateTime rawCreateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return rawCreateTime.format(formatter);
    }
}
