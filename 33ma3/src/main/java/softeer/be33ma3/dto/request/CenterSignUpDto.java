package softeer.be33ma3.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "센터 회원가입 요청 DTO")
public class CenterSignUpDto{
    @Schema(description = "로그인 아이디", example = "user1")
    @NotBlank(message = "아이디는 필수입니다.")
    private String loginId;

    @Schema(description = "비밀번호", example = "12345")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @Schema(description = "비밀번호", example = "12345")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String centerName;

    @Schema(description = "위도", example = "37.1234")
    @NotNull(message = "위도는 필수입니다.")
    private double latitude;

    @Schema(description = "경도", example = "127.1234")
    @NotNull(message = "경도는 필수입니다.")
    private double longitude;
}
