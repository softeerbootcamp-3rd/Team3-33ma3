package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import softeer.be33ma3.domain.Center;

@Data
@AllArgsConstructor
@Schema(description = "센터 정보 응답 DTO")
public class CenterListDto {
    @Schema(description = "센터 아이디", example = "1")
    private long centerId;

    @Schema(description = "위도", example = "37.12345")
    private double latitude;

    @Schema(description = "경도", example = "127.12345")
    private double longitude;

    public static CenterListDto from(Center center) {
        return new CenterListDto(center.getCenterId(), center.getLatitude(), center.getLongitude());
    }
}
