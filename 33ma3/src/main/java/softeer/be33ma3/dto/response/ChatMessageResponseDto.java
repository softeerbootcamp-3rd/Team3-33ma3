package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import softeer.be33ma3.domain.ChatMessage;

import static softeer.be33ma3.utils.StringParser.createTimeParsing;

@Data
@AllArgsConstructor
@Schema(description = "채팅 메세지 응답 DTO")
public class ChatMessageResponseDto {
    @Schema(description = "내용", example = "문의문의문의합니다")
    private String contents;
    @Schema(description = "메세지 보낸 시간", example = "오전 11:00")
    private String createTime;

    public static ChatMessageResponseDto create(ChatMessage chatMessage){
        return new ChatMessageResponseDto(chatMessage.getContents(), createTimeParsing(chatMessage.getCreateTime()));
    }
}
