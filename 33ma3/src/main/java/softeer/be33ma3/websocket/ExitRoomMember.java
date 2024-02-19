package softeer.be33ma3.websocket;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExitRoomMember {
    private String type;
    private Long roomId;
    private Long memberId;
}
