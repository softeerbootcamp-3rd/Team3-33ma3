package softeer.be33ma3.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ImageListDto {
    private List<Long> imageIds;

    public ImageListDto(List<Long> imageIds) {
        this.imageIds = imageIds;
    }
}
