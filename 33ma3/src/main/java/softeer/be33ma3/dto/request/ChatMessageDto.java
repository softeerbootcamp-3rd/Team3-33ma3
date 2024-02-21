package softeer.be33ma3.dto.request;
import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    // 메시지  타입 : 입장, 채팅
    public enum MessageType{
        TALK, EXIT
    }

    private MessageType messageType; //메시지 타입

    private Long roomId; //방 번호

    private Long senderId; //보내는 사람

    private Long receiverId; //받는 사람

    private String message; //메시지
}
