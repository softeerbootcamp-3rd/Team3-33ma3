package softeer.be33ma3.dto.request;

import lombok.Data;

@Data
public class MemberSignUpDto {
    private Integer memberType;
    private String loginId;
    private String password;
}
