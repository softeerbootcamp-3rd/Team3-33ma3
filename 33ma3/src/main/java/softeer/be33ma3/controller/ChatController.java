package softeer.be33ma3.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.dto.request.ChatMessageRequestDto;
import softeer.be33ma3.dto.response.ChatRoomListDto;
import softeer.be33ma3.jwt.CurrentUser;
import softeer.be33ma3.response.DataResponse;
import softeer.be33ma3.response.SingleResponse;
import softeer.be33ma3.service.ChatService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/chatRoom/{post_id}/{center_id}")
    public ResponseEntity<?> createRoom(@CurrentUser Member member, @PathVariable("center_id") Long centerId, @PathVariable("post_id") Long postId){
        Long roomId = chatService.createRoom(member, centerId, postId);

        return ResponseEntity.ok().body(DataResponse.success("채팅방 생성 성공", roomId));
    }

    @PostMapping("/chat/{room_id}/{receiver_id}")
    public ResponseEntity<?> chat(@Schema(hidden = true) @CurrentUser Member sender, @PathVariable("room_id") Long roomId,
                                  @PathVariable("receiver_id") Long receiverId, @RequestBody @Valid ChatMessageRequestDto chatMessageRequestDto){
        chatService.sendMessage(sender, roomId, receiverId, chatMessageRequestDto.getMessage());

        return ResponseEntity.ok().body(SingleResponse.success("문의 내역 전송 성공"));
    }

    @GetMapping("/chatRoom/all")
    public ResponseEntity<?> showAllChatRoom(@CurrentUser Member member){
        List<ChatRoomListDto> allChatRoomListDto = chatService.showAllChatRoom(member);

        return ResponseEntity.ok().body(DataResponse.success("채팅 내역 전송 성공", allChatRoomListDto));
    }
}
