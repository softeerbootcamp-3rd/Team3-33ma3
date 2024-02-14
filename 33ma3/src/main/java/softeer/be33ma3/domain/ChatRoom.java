package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ChatRoom extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chatRoomId;

    @ManyToOne
    @JoinColumn(name = "center_id")
    private Member center;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Member client;
}
