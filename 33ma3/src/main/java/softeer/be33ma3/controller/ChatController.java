package softeer.be33ma3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.dto.response.ChatHistoryDto;
import softeer.be33ma3.dto.response.AllChatRoomDto;
import softeer.be33ma3.jwt.CurrentUser;
import softeer.be33ma3.response.DataResponse;
import softeer.be33ma3.response.SingleResponse;
import softeer.be33ma3.service.ChatService;

import java.util.List;

@Tag(name = "문의", description = "문의 내역, 채팅 관련 api")
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅방 생성 성공", content = @Content(schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "401", description = "게시글 작성자만 생성할 수 있습니다.", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글" + "<br>존재하지 않는 센터", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Parameter(name = "post_id", description = "게시글 id", required = true, example = "1", in = ParameterIn.PATH)
    @Parameter(name = "center_id", description = "문의할 센터 id", required = true, example = "2", in = ParameterIn.PATH)
    @Operation(summary = "채팅방 생성", description = "채팅방 생성하기 메서드 입니다.")
    @PostMapping("/chatRoom/{post_id}/{center_id}")
    public ResponseEntity<?> createRoom(@CurrentUser Member member, @PathVariable("center_id") Long centerId, @PathVariable("post_id") Long postId){
        Long roomId = chatService.createRoom(member, centerId, postId);

        return ResponseEntity.ok().body(DataResponse.success("채팅방 생성 성공", roomId));
    }

    @ApiResponse(responseCode = "200", description = "문의 내역 전송 성공", content = @Content(schema = @Schema(implementation = DataResponse.class)))
    @Operation(summary = "문의 내역", description = "문의 내역 보기 메서드 입니다.")
    @GetMapping("/chatRoom/all")
    public ResponseEntity<?> showAllChatRoom(@Schema(hidden = true) @CurrentUser Member member){
        List<AllChatRoomDto> allChatRoomDtos = chatService.showAllChatRoom(member);

        return ResponseEntity.ok().body(DataResponse.success("문의 내역 전송 성공", allChatRoomDtos));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅 내역 전송 성공", content = @Content(schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "401", description = "해당 방의 회원이 아닙니다.", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 채팅 룸", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Operation(summary = "채팅 내역 조회", description = "채팅 내역을 조회하는 메서드 입니다.")
    @GetMapping("/chat/history/{room_id}")
    public ResponseEntity<?> showOneChatHistory(@Schema(hidden = true) @CurrentUser Member member, @PathVariable("room_id") Long roomId){
         List<ChatHistoryDto> chatHistoryDtos = chatService.showOneChatHistory(member, roomId);

        return ResponseEntity.ok().body(DataResponse.success("채팅 내역 전송 성공", chatHistoryDtos));
    }
}
