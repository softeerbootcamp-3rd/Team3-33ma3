package softeer.be33ma3.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import softeer.be33ma3.domain.*;
import softeer.be33ma3.repository.ChatMessageRepository;
import softeer.be33ma3.repository.ChatRoomRepository;
import softeer.be33ma3.repository.MemberRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
class ChatMessageRepositoryTest {
    @Autowired private ChatMessageRepository chatMessageRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private ChatRoomRepository chatRoomRepository;

    @BeforeEach
    void setUp(){
        Member member1 = new Member(1, "client1", "1234", null);
        Member member2 = new Member(1, "center1", "1234", null);
        memberRepository.saveAll(List.of(member1, member2));
    }

    @AfterEach
    void tearDown() {
        chatMessageRepository.deleteAllInBatch();
        chatRoomRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("읽지 않은 메세지 수를 계산한다.")
    @Test
    void countReadDoneIsFalse(){
        //given
        Member client = memberRepository.findMemberByLoginId("client1").get();
        Member center = memberRepository.findMemberByLoginId("center1").get();

        ChatRoom savedChatRoom = createChatMessage(client, center);

        //when
        long count = chatMessageRepository.countReadDoneIsFalse(savedChatRoom.getChatRoomId(), center.getMemberId());

        //then
        assertThat(count).isEqualTo(2);
    }

    @DisplayName("채팅방 아이디로 해당 채팅방의 메세지들을 찾을 수 있다.")
    @Test
    void findByChatRoom_ChatRoomId(){
        //given
        Member client = memberRepository.findMemberByLoginId("client1").get();
        Member center = memberRepository.findMemberByLoginId("center1").get();

        ChatRoom chatRoom = ChatRoom.createChatRoom(client, center);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        ChatMessage chatMessage1 = ChatMessage.createChatMessage(client, savedChatRoom, "첫번째 메세지");
        ChatMessage chatMessage2 = ChatMessage.createChatMessage(center, savedChatRoom, "마지막 메세지");

        chatMessageRepository.saveAll(List.of(chatMessage1, chatMessage2));

        //when
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoom_ChatRoomId(savedChatRoom.getChatRoomId());

        //then
        assertThat(chatMessages).hasSize(2);
    }

    private ChatRoom createChatMessage(Member client, Member center) {
        ChatRoom chatRoom = ChatRoom.createChatRoom(client, center);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        ChatMessage chatMessage1 = ChatMessage.createChatMessage(client, savedChatRoom, "1");
        ChatMessage chatMessage2 = ChatMessage.createChatMessage(client, savedChatRoom, "2");
        chatMessageRepository.saveAll(List.of(chatMessage1, chatMessage2));
        return savedChatRoom;
    }
}
