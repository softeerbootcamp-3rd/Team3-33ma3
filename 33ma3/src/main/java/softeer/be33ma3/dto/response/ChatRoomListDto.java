package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import softeer.be33ma3.domain.ChatRoom;

@Data
@Schema(description = "문의 내역 리스트 응답")
public class ChatRoomListDto {
    @Schema(description = "방 아이디", example = "1")
    private Long roomId;
    @Schema(description = "센터 아이디", example = "1")
    private Long centerId;
    @Schema(description = "사용자 아이디", example = "2")
    private Long clientId;
    @Schema(description = "채팅한 상대 이름", example = "현대자동차 강남점")
    private String memberName;      // 상대방 이름
    @Schema(description = "마지막 메세지", example = "네 답변드립니다")
    private String lastMessage;     // 마지막 메세지
    @Schema(description = "읽지 않은 메세지 개수", example = "5")
    private int noReadCount;        // 읽지 않은 메세지 개수

    public static ChatRoomListDto createChatRoomDto(ChatRoom chatRoom, String lastMessage, String memberName, int noReadCount) {
        ChatRoomListDto chatRoomListDto = new ChatRoomListDto();
        chatRoomListDto.roomId = chatRoom.getChatRoomId();
        chatRoomListDto.centerId = chatRoom.getCenter().getMemberId();
        chatRoomListDto.clientId = chatRoom.getClient().getMemberId();
        chatRoomListDto.memberName = memberName;
        chatRoomListDto.lastMessage = lastMessage;
        chatRoomListDto.noReadCount = noReadCount;

        return chatRoomListDto;
    }
}
