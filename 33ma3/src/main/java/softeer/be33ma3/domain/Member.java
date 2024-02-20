package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private int memberType;     // 클라이언트: 1, 서비스 센터: 2

    private String loginId;

    private String password;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    private String refreshToken;

    public Member(int memberType, String loginId, String password) {
        this.memberType = memberType;
        this.loginId = loginId;
        this.password = password;
    }

    public static Member createMember(int memberType, String loginId, String password){
        return new Member(memberType, loginId, password);
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setProfile(Image image) {
        this.image = image;
    }
}
