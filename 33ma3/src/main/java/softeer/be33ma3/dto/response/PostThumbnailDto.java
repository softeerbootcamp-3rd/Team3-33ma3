package softeer.be33ma3.dto.response;

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
public class PostThumbnailDto implements Comparable<PostThumbnailDto> {

    private Long postId;
    private String modelName;
    private LocalDateTime rawCreateTime;
    private String createTime;
    private int dDay;
    private int remainTime;
    private List<String> imageList;
    private List<String> repairList;
    private List<String> tuneUpList;
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
