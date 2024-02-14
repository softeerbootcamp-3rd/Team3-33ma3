package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.ChatMessage;
import softeer.be33ma3.domain.ChatRoom;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.exception.UnauthorizedException;
import softeer.be33ma3.repository.*;
import softeer.be33ma3.websocket.WebSocketHandler;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final WebSocketHandler webSocketHandler;

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
    public void createMessage(Member sender, Long roomId, String contents) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅 룸"));
        ChatMessage chatMessage = ChatMessage.createChatMessage(sender, chatRoom, contents);

        chatMessageRepository.save(chatMessage);
    }

    public void sendMessage(Long roomId, Long receiverId, String contents) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅 룸"));
        Member receiver = memberRepository.findById(receiverId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        webSocketHandler.sendData2Client(receiver.getMemberId(), contents);
    }
}

