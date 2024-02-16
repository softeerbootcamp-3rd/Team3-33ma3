package softeer.be33ma3.dto.response;

import lombok.Data;
import softeer.be33ma3.domain.ChatRoom;

@Data
public class ChatDto {
    private Long roomId;
    private Long centerId;
    private Long clientId;
    private String lastMessage;     // 마지막 메세지
    private String memberName;      // 상대방 이름
    private int noReadCount;        // 읽지 않은 메세지 개수

    public static ChatDto createChatDto(ChatRoom chatRoom, String lastMessage, String memberName, int noReadCount) {
        ChatDto chatDto = new ChatDto();
        chatDto.roomId = chatRoom.getChatRoomId();
        chatDto.centerId = chatRoom.getCenter().getMemberId();
        chatDto.clientId = chatRoom.getClient().getMemberId();
        chatDto.lastMessage = lastMessage;
        chatDto.memberName = memberName;
        chatDto.noReadCount = noReadCount;

        return chatDto;
    }
}
