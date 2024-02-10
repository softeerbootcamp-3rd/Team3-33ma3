package softeer.be33ma3.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClientSignUpDto {
    @Schema(description = "멤버 타입", example = "1")
    @NotNull
    private Integer memberType;

    @Schema(description = "로그인 아이디", example = "user1")
    @NotBlank(message = "아이디는 필수입니다.")
    private String loginId;

    @Schema(description = "비밀번호", example = "12345")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
