package softeer.be33ma3.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import softeer.be33ma3.domain.ChatMessage;
import softeer.be33ma3.domain.ChatRoom;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.dto.request.PostCreateDto;
import softeer.be33ma3.repository.*;
import softeer.be33ma3.repository.post.PostRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ChatServiceTest {
    @Autowired ChatService chatService;
    @Autowired MemberRepository memberRepository;
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired PostRepository postRepository;
    @Autowired ChatMessageRepository chatMessageRepository;

    @AfterEach
    void tearDown() {
        chatMessageRepository.deleteAllInBatch();
        chatRoomRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("채팅을 할 채팅방을 만든다.")
    @Test
    void createRoom(){
        //given
        Member client = new Member(1, "client1", "1234");
        Member savedClient = memberRepository.save(client);
        Member center = new Member(2, "center1", "1234");
        Member savedCenter = memberRepository.save(center);

        Long postId = createPost(savedClient).getPostId();

        //when
        Long roomId = chatService.createRoom(savedClient, savedCenter.getMemberId(), postId);

        //then
        assertThat(roomId).isNotNull();
    }

    @DisplayName("채팅방에서 메세지를 전송할 수 있다.")
    @Test
    void sendMessage(){
        //given
        Member client = new Member(1, "client1", "1234");
        Member savedClient = memberRepository.save(client);
        Member center = new Member(2, "center1", "1234");
        Member savedCenter = memberRepository.save(center);

        ChatRoom chatRoom = ChatRoom.createChatRoom(savedClient, savedCenter);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        //when
        chatService.sendMessage(savedClient, savedChatRoom.getChatRoomId(), savedCenter.getMemberId(), "메세지 내용");

        //then
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoom_ChatRoomId(savedChatRoom.getChatRoomId());
        assertThat(chatMessages).hasSize(1)
                .extracting("contents")
                .containsExactly("메세지 내용");
    }

    private Post createPost(Member savedClient) {
        PostCreateDto postCreateDto = new PostCreateDto("승용차", "제네시스", 3, "서울시 강남구", "기스, 깨짐", "오일 교체", new ArrayList<>(),"수정후 내용");
        Post post = Post.createPost(postCreateDto, null, savedClient);
        return postRepository.save(post);
    }
}
