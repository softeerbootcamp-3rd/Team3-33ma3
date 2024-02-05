package softeer.be33ma3.dto.request;

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
public class PostCreateDto {
    @NotNull(message = "멤버 아이디는 필수입니다.")
    private long memberId;
    @NotBlank(message = "차종은 필수입니다.")
    private String carType;
    @NotBlank(message = "모델명은 필수입니다.")
    private String modelName;
    @NotNull(message = "마감기한은 필수입니다.")
    private int deadline;
    @NotBlank(message = "위치는 필수입니다.")
    private String location;
    @NotBlank(message = "수리 서비스는 필수입니다.")
    private String repairService;
    @NotNull
    private String tuneUpService;
    @NotNull(message = "주위 센터 정보는 필수입니다.")
    private List<Long> centers;
    @NotNull
    private List<Long> images;
    @Length(max=10, message = "내용은 최대 50글자입니다.")
    @NotNull
    private String contents;
}
