package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
@Schema(description = "평균 제시가격 응답 DTO")
public class AvgPriceDto {
    @Schema(description = "평균 제시가격", example = "10.5")
    private double avgPrice;
}
