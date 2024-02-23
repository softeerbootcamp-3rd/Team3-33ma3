package softeer.be33ma3.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LastMessageDto {
    private String message;
    private LocalDateTime createTime;
}
