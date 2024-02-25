package softeer.be33ma3.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ExitMember {
    @JsonProperty("type")
    private String type;
    @JsonProperty("memberId")
    private Long memberId;
}
