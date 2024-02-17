package softeer.be33ma3.dto.response;

import lombok.Builder;
import lombok.Data;
import softeer.be33ma3.domain.ChatMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class ChatHistoryDto {
    private Long senderId;
    private String contents;
    private String createTime;
    private boolean readDone;

    public static ChatHistoryDto getChatHistoryDto(ChatMessage chatMessage){
        return ChatHistoryDto.builder()
                .senderId(chatMessage.getSender().getMemberId())
                .contents(chatMessage.getContents())
                .createTime(createTimeFormatting(chatMessage.getCreateTime()))
                .readDone(chatMessage.isReadDone())
                .build();
    }

    private static String createTimeFormatting(LocalDateTime createTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return createTime.format(formatter);
    }
}
