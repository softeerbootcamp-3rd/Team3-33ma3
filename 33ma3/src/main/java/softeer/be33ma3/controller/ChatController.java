package softeer.be33ma3.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.dto.request.ChatMessageDto;
import softeer.be33ma3.jwt.CurrentUser;
import softeer.be33ma3.response.DataResponse;
import softeer.be33ma3.response.SingleResponse;
import softeer.be33ma3.service.ChatService;

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
                                  @PathVariable("receiver_id") Long receiverId, @RequestBody @Valid ChatMessageDto chatMessageDto){
        chatService.createMessage(sender, roomId, chatMessageDto.getMessage());
        chatService.sendMessage(roomId, receiverId, chatMessageDto.getMessage());

        return ResponseEntity.ok().body(SingleResponse.success("전송 성공"));
    }
}
