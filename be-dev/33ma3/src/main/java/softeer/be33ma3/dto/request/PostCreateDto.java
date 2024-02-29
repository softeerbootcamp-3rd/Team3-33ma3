package softeer.be33ma3.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "게시글 작성 요청 DTO")
public class PostCreateDto {
    @Schema(description = "차종", example = "승용차")
    @NotBlank(message = "차종은 필수입니다.")
    private String carType;

    @Schema(description = "모델명", example = "제네시스")
    @NotBlank(message = "모델명은 필수입니다.")
    private String modelName;

    @Schema(description = "마감기한", example = "2")
    @NotNull(message = "마감기한은 필수입니다.")
    @Max(value = 10, message = "최대 10일까지 가능합니다.")
    private Integer deadline;

    @Schema(description = "사용자 위치", example = "서울시 강남구")
    @NotBlank(message = "위치는 필수입니다.")
    private String location;

    @Schema(description = "수리 서비스 종류", example = "기스, 깨짐")
    private String repairService;

    @Schema(description = "정비 서비스", example = "오일 교체, 타이어 교체")
    private String tuneUpService;

    @Schema(description = "주위 센터 아이디", example = "[1, 2, 3]")
    @NotNull(message = "주위 센터 정보는 필수입니다.")
    private List<Long> centers;

    @Schema(description = "내용", example = "기스났는데 얼마할까요?")
    @Length(max=500, message = "내용은 최대 500글자입니다.")
    @NotNull
    private String contents;
}
