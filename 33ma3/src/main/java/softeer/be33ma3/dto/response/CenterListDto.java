package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import softeer.be33ma3.domain.Center;

@Data
@AllArgsConstructor
@Schema(description = "센터 정보 응답 DTO")
public class CenterListDto {
    private long centerId;

    private String centerName;

    private double latitude;

    private double longitude;

    public static CenterListDto from(Center center) {
        return new CenterListDto(center.getCenterId(), center.getCenterName(), center.getLatitude(), center.getLongitude());
    }
}
