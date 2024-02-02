package softeer.be33ma3.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;
    private int memberType;     // 클라이언트: 1, 서비스 센터: 2
    private String loginId;
    private String password;
}
