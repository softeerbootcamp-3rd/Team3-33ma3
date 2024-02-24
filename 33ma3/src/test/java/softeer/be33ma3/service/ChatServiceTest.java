package softeer.be33ma3.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import softeer.be33ma3.dto.response.AllChatRoomDto;
import softeer.be33ma3.dto.response.ChatHistoryDto;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.exception.ErrorCode;
import softeer.be33ma3.repository.*;
import softeer.be33ma3.repository.Chat.ChatMessageRepository;
import softeer.be33ma3.repository.Chat.ChatRoomRepository;
import softeer.be33ma3.repository.post.PostRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static softeer.be33ma3.exception.ErrorCode.ONLY_POST_AUTHOR_ALLOWED;

@SpringBootTest
@ActiveProfiles("test")
class ChatServiceTest {
    @Autowired ChatService chatService;
    @Autowired MemberRepository memberRepository;
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired PostRepository postRepository;
    @Autowired ChatMessageRepository chatMessageRepository;

    @BeforeEach
    void setUp(){
        Member member1 = new Member(1, "client1", "1234", null);
        Member member2 = new Member(1, "center1", "1234", null);
        Member other = new Member(1, "other", "1234", null);
        memberRepository.saveAll(List.of(member1, member2, other));
    }
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
        Member client = memberRepository.findMemberByLoginId("client1").get();
        Member center = memberRepository.findMemberByLoginId("center1").get();

        Long postId = createPost(client).getPostId();

        //when
        Long roomId = chatService.createRoom(client, center.getMemberId(), postId);

        //then
        assertThat(roomId).isNotNull();
    }

    @DisplayName("게시글 작성자가 아닌 다른 사람이 채팅방을 만들면 예외가 발생한다.")
    @Test
    void creatRoomOtherMember(){
        //given
        Member client = memberRepository.findMemberByLoginId("client1").get();
        Member other = memberRepository.findMemberByLoginId("other").get();
        Member center = memberRepository.findMemberByLoginId("center1").get();

        Long postId = createPost(client).getPostId();

        //when //then
        assertThatThrownBy(() -> chatService.createRoom(other, center.getMemberId(), postId))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ONLY_POST_AUTHOR_ALLOWED);
    }

    @DisplayName("이미 같은 일반 사용자와 센터의 방이 존재하는 경우 기존 아이디를 반환한다.")
    @Test
    void createRoomExist(){
        //given
        Member client = memberRepository.findMemberByLoginId("client1").get();
        Member center = memberRepository.findMemberByLoginId("center1").get();

        ChatRoom chatRoom = ChatRoom.createChatRoom(client, center);
        Long existChatRoomId = chatRoomRepository.save(chatRoom).getChatRoomId();
        Long postId = createPost(client).getPostId();

        //when
        Long roomId = chatService.createRoom(client, center.getMemberId(), postId);

        //then
        assertThat(roomId).isEqualTo(existChatRoomId);
    }

    @DisplayName("해당 사용자의 모든 채팅방을 정보를 반환한다.")
    @Test
    void showAllChatRoom(){
        //given
        Member client = memberRepository.findMemberByLoginId("client1").get();
        Member center = memberRepository.findMemberByLoginId("center1").get();

        ChatRoom chatRoom = ChatRoom.createChatRoom(client, center);
        chatRoomRepository.save(chatRoom);

        //when
        List<AllChatRoomDto> allChatRoomDtos = chatService.showAllChatRoom(client);

        //then
        assertThat(allChatRoomDtos).hasSize(1)
                .extracting("memberName")   //상대방 이름
                .containsExactly("center1");
    }

    @DisplayName("기존 채팅방의 채팅 내역들을 정보를 반환한다.")
    @Test
    void showOneChatHistory(){
        //given
        Member client = memberRepository.findMemberByLoginId("client1").get();
        Member center = memberRepository.findMemberByLoginId("center1").get();

        ChatRoom chatRoom = ChatRoom.createChatRoom(client, center);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        ChatMessage chatMessage1 = ChatMessage.createChatMessage(client, savedChatRoom, "첫번째 메세지");
        ChatMessage chatMessage2 = ChatMessage.createChatMessage(center, savedChatRoom, "두번째 메세지");

        chatMessageRepository.saveAll(List.of(chatMessage1, chatMessage2));

        //when
        List<ChatHistoryDto> chatHistoryDtos = chatService.showOneChatHistory(client, savedChatRoom.getChatRoomId());

        //then
        assertThat(chatHistoryDtos).hasSize(2)
                .extracting("contents")
                .containsExactly("첫번째 메세지", "두번째 메세지");
    }

    @DisplayName("채팅방 아이디가 없는 아이디인 경우 예외가 발생한다.")
    @Test
    void showOneChatHistoryWithNotFoundChatRoomId(){
        //given
        Member client = memberRepository.findMemberByLoginId("client1").get();

        Long mockRoomId = 1L;

        //when //then
        assertThatThrownBy(() -> chatService.showOneChatHistory(client, mockRoomId))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_FOUND_CHAT_ROOM);
    }

    @DisplayName("해당방의 멤버가 아닌 경우 예외가 발생한다.")
    @Test
    void showOneChatHistoryWithOtherMember(){
        Member client = memberRepository.findMemberByLoginId("client1").get();
        Member center = memberRepository.findMemberByLoginId("center1").get();
        Member other = memberRepository.findMemberByLoginId("other").get();

        ChatRoom chatRoom = ChatRoom.createChatRoom(client, center);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        //when //then
        assertThatThrownBy(() -> chatService.showOneChatHistory(other, savedChatRoom.getChatRoomId()))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_A_MEMBER_OF_ROOM);
    }

    private Post createPost(Member savedClient) {
        PostCreateDto postCreateDto = new PostCreateDto("승용차", "제네시스", 3, "서울시 강남구", "기스, 깨짐", "오일 교체", new ArrayList<>(),"수정후 내용");
        Post post = Post.createPost(postCreateDto, null, savedClient);
        return postRepository.save(post);
    }
}
