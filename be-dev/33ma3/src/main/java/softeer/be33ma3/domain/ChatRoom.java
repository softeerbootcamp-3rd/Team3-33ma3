package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ChatRoom extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private Member center;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Member client;

    public static ChatRoom createChatRoom(Member client, Member center) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.client = client;
        chatRoom.center = center;

        return chatRoom;
    }
}
