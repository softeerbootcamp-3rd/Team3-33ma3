package softeer.be33ma3.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import softeer.be33ma3.domain.ChatMessage;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatMessageResponseDto {
    private String contents;
    private LocalDateTime createTime;

    public static ChatMessageResponseDto createChatMessage(ChatMessage chatMessage){
        return new ChatMessageResponseDto(chatMessage.getContents(), chatMessage.getCreateTime());
    }
}

