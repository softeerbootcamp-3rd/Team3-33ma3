package softeer.be33ma3.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import softeer.be33ma3.domain.Center;

@Data
@AllArgsConstructor
public class CenterListDto {
    private long centerId;

    private String centerName;

    private double latitude;

    private double longitude;

    public static CenterListDto from(Center center) {
        return new CenterListDto(center.getCenterId(), center.getCenterName(), center.getLatitude(), center.getLongitude());
    }
}
