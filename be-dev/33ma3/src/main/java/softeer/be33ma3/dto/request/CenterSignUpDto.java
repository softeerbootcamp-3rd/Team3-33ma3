package softeer.be33ma3.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "센터 회원가입 요청 DTO")
@NoArgsConstructor
public class CenterSignUpDto extends ClientSignUpDto {

    @Schema(description = "위도", example = "37.1234")
    @NotNull(message = "위도는 필수입니다.")
    private double latitude;

    @Schema(description = "경도", example = "127.1234")
    @NotNull(message = "경도는 필수입니다.")
    private double longitude;

    public CenterSignUpDto(String loginId, String password, double latitude, double longitude) {
        super(loginId, password);
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
