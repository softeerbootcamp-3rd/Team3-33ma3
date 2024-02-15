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
    @JoinColumn(name = "receiver_id")
    private Member receiver;  //수신자

    public static Alert createAlert(Long roomId, Member member){
        Alert alert = new Alert();
        alert.contents = "수리수리마수리 답장이 왔어요~";
        alert.readDone = false;
        alert.roomId = roomId;
        alert.receiver = member;

        return alert;
    }
}
