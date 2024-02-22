package softeer.be33ma3.dto.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    @JsonProperty("roomId")
    private Long roomId; //방 번호
    @JsonProperty("senderId")
    private Long senderId; //보내는 사람
    @JsonProperty("receiverId")
    private Long receiverId; //받는 사람
    @JsonProperty("message")
    private String message; //메시지
}
