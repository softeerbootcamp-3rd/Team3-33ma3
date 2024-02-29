package softeer.be33ma3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import softeer.be33ma3.domain.ChatRoom;

import java.time.LocalDateTime;

import static softeer.be33ma3.utils.StringParser.createTimeParsing;

@Data
@Schema(description = "문의 내역 리스트 응답")
public class AllChatRoomDto {
    @Schema(description = "방 아이디", example = "1")
    private Long roomId;
    @Schema(description = "센터 아이디", example = "1")
    private Long centerId;
    @Schema(description = "센터 프로필", example = "image.png")
    private String centerProfile;
    @Schema(description = "사용자 아이디", example = "2")
    private Long clientId;
    @Schema(description = "클라이언트 아이디", example = "1")
    private String clientProfile;
    @Schema(description = "채팅한 상대 이름", example = "현대자동차 강남점")
    private String memberName;      // 상대방 이름
    @Schema(description = "마지막 메세지", example = "네 답변드립니다")
    private String lastMessage;     // 마지막 메세지
    @Schema(description = "읽지 않은 메세지 개수", example = "5")
    private int noReadCount;        // 읽지 않은 메세지 개수
    @Schema(description = "메세지 생성 시간", example = "오전 07:12")
    private String createTime;

    public static AllChatRoomDto create(ChatRoom chatRoom, String lastMessage, String memberName, int noReadCount, LocalDateTime createTime) {
        AllChatRoomDto allChatRoomDto = new AllChatRoomDto();
        allChatRoomDto.roomId = chatRoom.getChatRoomId();
        allChatRoomDto.centerId = chatRoom.getCenter().getMemberId();
        allChatRoomDto.centerProfile = chatRoom.getCenter().getImage().getLink();
        allChatRoomDto.clientId = chatRoom.getClient().getMemberId();
        allChatRoomDto.clientProfile = chatRoom.getClient().getImage().getLink();
        allChatRoomDto.memberName = memberName;
        allChatRoomDto.lastMessage = lastMessage;
        allChatRoomDto.noReadCount = noReadCount;
        allChatRoomDto.createTime = createTimeParsing(createTime);

        return allChatRoomDto;
    }
}
