package softeer.be33ma3.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "채팅 메세지 요청 DTO")
public class ChatMessageRequestDto {
    @Schema(description = "message 내용", example = "문의합니다~")
    @NotBlank(message = "메세지는 필수입니다.")
    private String message;
}
