package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    private static final int CLIENT_TYPE = 1;
    private static final int CENTER_TYPE = 2;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private int memberType;     // 클라이언트: 1, 서비스 센터: 2

    private String loginId;

    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    private String refreshToken;

    public Member(int memberType, String loginId, String password, Image image) {
        this.memberType = memberType;
        this.loginId = loginId;
        this.password = password;
        this.image = image;
    }

    public static Member createClient(String loginId, String password, Image image){
        return new Member(CLIENT_TYPE, loginId, password, image);
    }

    public static Member createCenter(String loginId, String password, Image image){
        return new Member(CENTER_TYPE, loginId, password, image);
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setProfile(Image image) {
        this.image = image;
    }

    public boolean isClient(){
        return memberType == CLIENT_TYPE;
    }

    public boolean isCenter(){
        return memberType == CENTER_TYPE;
    }
}
