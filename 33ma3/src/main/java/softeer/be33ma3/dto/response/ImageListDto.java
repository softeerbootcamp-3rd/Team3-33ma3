package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "이미지 아이디 리스트 응답 DTO")
public class ImageListDto {
    private List<Long> imageIds;

    public ImageListDto(List<Long> imageIds) {
        this.imageIds = imageIds;
    }
}
