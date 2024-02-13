package softeer.be33ma3.websocket;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExitUser {
    private String type;
    private Long roomId;
    private Long memberId;
}
