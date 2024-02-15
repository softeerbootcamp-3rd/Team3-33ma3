package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.*;
import softeer.be33ma3.dto.response.ChatMessageDto;
import softeer.be33ma3.exception.UnauthorizedException;
import softeer.be33ma3.repository.*;
import softeer.be33ma3.websocket.WebSocketHandler;
import softeer.be33ma3.websocket.WebSocketRepository;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final WebSocketHandler webSocketHandler;
    private final WebSocketRepository webSocketRepository;
    private final AlertRepository alertRepository;

    @Transactional
    public Long createRoom(Member client, Long centerId, Long postId) {
        Member center = memberRepository.findById(centerId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 센터"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글"));

        if(!post.getMember().equals(client)){
            throw new UnauthorizedException("작성자만 문의할 수 있습니다.");
        }

        ChatRoom chatRoom = ChatRoom.createCenter(client, center);
        return chatRoomRepository.save(chatRoom).getChatRoomId();
    }

    @Transactional
    public void sendMessage(Member sender, Long roomId, Long receiverId, String contents) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅 룸"));
        Member receiver = memberRepository.findById(receiverId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
        ChatMessage chatMessage = ChatMessage.createChatMessage(sender, chatRoom, contents);
        chatMessage = chatMessageRepository.save(chatMessage);

        if(webSocketRepository.findSessionByMemberId(receiver.getMemberId()) == null){  //채팅룸에 상대방이 존재하지 않을 경우
            chatMessage.setReadDoneFalse();   //안읽음 처리
            Alert alert = Alert.createAlert(chatRoom.getChatRoomId(), receiver);  //알림 테이블에 저장
            alertRepository.save(alert);
            return;
        }

        ChatMessageDto chatMessageDto = ChatMessageDto.createChatMessage(chatMessage);
        webSocketHandler.sendData2Client(receiver.getMemberId(), chatMessageDto);
    }
}
