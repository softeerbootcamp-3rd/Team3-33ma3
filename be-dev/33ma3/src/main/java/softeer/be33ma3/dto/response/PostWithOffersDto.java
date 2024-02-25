package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "게시글과 댓글 목록 DTO")
public class PostWithOffersDto {
    @Schema(description = "게시글 세부사항")
    private PostDetailDto postDetail;
    @Schema(description = "게시글에 달린 댓글 리스트")
    private List<OfferDetailDto> offerDetails;
}
