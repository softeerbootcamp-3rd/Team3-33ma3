package softeer.be33ma3.websocket;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExitMember {
    private String type;
    private Long roomId;
    private Long memberId;
}
