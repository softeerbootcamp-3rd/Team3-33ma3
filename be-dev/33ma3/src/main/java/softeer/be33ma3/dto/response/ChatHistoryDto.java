package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import softeer.be33ma3.domain.ChatMessage;

import java.time.LocalDateTime;

import static softeer.be33ma3.utils.StringParser.createTimeParsing;

@Data
@Builder
@Schema(description = "채팅 내역")
public class ChatHistoryDto {
    @Schema(description = "보내는 사람 아이디", example = "1")
    private Long senderId;
    @Schema(description = "메세지 내용", example = "안녕하세요")
    private String contents;
    @Schema(description = "메세지 생성 시간", example = "오전 07:12")
    private String createTime;
    @Schema(description = "읽음 여부", example = "1")
    private boolean readDone;

    public static ChatHistoryDto getChatHistoryDto(ChatMessage chatMessage, LocalDateTime createTime){
        return ChatHistoryDto.builder()
                .senderId(chatMessage.getSender().getMemberId())
                .contents(chatMessage.getContents())
                .createTime(createTimeParsing(createTime))
                .readDone(chatMessage.isReadDone())
                .build();
    }
}
