package softeer.be33ma3.dto.response;

import lombok.Data;
import softeer.be33ma3.domain.ChatRoom;

@Data
public class ChatRoomListDto {
    private Long roomId;
    private Long centerId;
    private Long clientId;
    private String lastMessage;     // 마지막 메세지
    private String memberName;      // 상대방 이름
    private int noReadCount;        // 읽지 않은 메세지 개수

    public static ChatRoomListDto createChatRoomDto(ChatRoom chatRoom, String lastMessage, String memberName, int noReadCount) {
        ChatRoomListDto chatRoomListDto = new ChatRoomListDto();
        chatRoomListDto.roomId = chatRoom.getChatRoomId();
        chatRoomListDto.centerId = chatRoom.getCenter().getMemberId();
        chatRoomListDto.clientId = chatRoom.getClient().getMemberId();
        chatRoomListDto.lastMessage = lastMessage;
        chatRoomListDto.memberName = memberName;
        chatRoomListDto.noReadCount = noReadCount;

        return chatRoomListDto;
    }
}
