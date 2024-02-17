package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import softeer.be33ma3.domain.ChatMessage;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(description = "채팅 메세지 응답")
public class ChatMessageResponseDto {
    @Schema(description = "내용", example = "문의문의문의합니다")
    private String contents;
    @Schema(description = "메세지 보낸 시간", example = "2024-02-06T02:23:25.043239")
    private LocalDateTime createTime;

    public static ChatMessageResponseDto create(ChatMessage chatMessage){
        return new ChatMessageResponseDto(chatMessage.getContents(), chatMessage.getCreateTime());
    }
}
