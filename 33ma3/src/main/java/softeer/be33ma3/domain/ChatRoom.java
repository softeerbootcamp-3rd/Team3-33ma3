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

    public static ChatRoom createCenter(Member client, Member center) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.client = client;
        chatRoom.center = center;

        return chatRoom;
    }
}
