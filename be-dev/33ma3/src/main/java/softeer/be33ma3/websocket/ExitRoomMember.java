package softeer.be33ma3.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExitRoomMember {
    @JsonProperty("type")
    private String type;
    @JsonProperty("roomId")
    private Long roomId;
    @JsonProperty("memberId")
    private Long memberId;
}
