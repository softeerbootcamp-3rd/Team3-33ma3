package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static Alert createAlert(String contents, Long roomId, Member member){
        Alert alert = new Alert();
        alert.contents = contents;
        alert.readDone = false;
        alert.roomId = roomId;
        alert.receiver = member;

        return alert;
    }
}
