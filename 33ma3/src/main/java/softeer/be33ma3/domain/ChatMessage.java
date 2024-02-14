package softeer.be33ma3.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ChatMessage extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chatMessageId;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    private String contents;

    public static ChatMessage createChatMessage(Member member, ChatRoom chatRoom, String contents) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.sender = member;
        chatMessage.chatRoom = chatRoom;
        chatMessage.contents = contents;

        return chatMessage;
    }
}
