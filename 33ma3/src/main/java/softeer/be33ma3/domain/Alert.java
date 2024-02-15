package softeer.be33ma3.domain;

import jakarta.persistence.*;

@Entity
public class Alert extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AlertId;

    private String contents;

    private boolean readDone;

    private Long roomId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;  //수신자
}
