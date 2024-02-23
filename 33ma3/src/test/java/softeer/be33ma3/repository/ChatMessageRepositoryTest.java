//package softeer.be33ma3.repository;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import softeer.be33ma3.domain.*;
//import softeer.be33ma3.repository.Chat.ChatMessageRepository;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//@SpringBootTest
//@ActiveProfiles("test")
//class ChatMessageRepositoryTest {
//    @Autowired private ChatMessageRepository chatMessageRepository;
//    @Autowired private MemberRepository memberRepository;
//    @Autowired private ChatRoomRepository chatRoomRepository;
//
//    @BeforeEach
//    void setUp(){
//        Member member1 = new Member(1, "client1", "1234");
//        Member member2 = new Member(2, "center1", "1234");
//        memberRepository.saveAll(List.of(member1, member2));
//    }
//
//    @AfterEach
//    void tearDown() {
//        chatMessageRepository.deleteAllInBatch();
//        chatRoomRepository.deleteAllInBatch();
//        memberRepository.deleteAllInBatch();
//    }
//
//    @DisplayName("읽지 않은 메세지 수를 계산한다.")
//    @Test
//    void countReadDoneIsFalse(){
//        //given
//        Member client = memberRepository.findMemberByLoginId("client1").get();
//        Member center = memberRepository.findMemberByLoginId("center1").get();
//
//        ChatRoom savedChatRoom = createChatMessage(client, center);
//
//        //when
//        long count = chatMessageRepository.countReadDoneIsFalse(savedChatRoom.getChatRoomId(), center.getMemberId());
//
//        //then
//        assertThat(count).isEqualTo(2);
//    }
//
//    @DisplayName("마지막으로 작성된 메세지를 찾을 수 있다.")
//    @Test
//    void findLastMessageByChatRoomId(){
//        //given
//        Member client = memberRepository.findMemberByLoginId("client1").get();
//        Member center = memberRepository.findMemberByLoginId("center1").get();
//
//        ChatRoom savedChatRoom = createChatMessage(client, center);
//
//        //when
//        String lastMessage = chatMessageRepository.findLastMessageByChatRoomId(savedChatRoom.getChatRoomId());
//
//        //then
//        assertThat(lastMessage).isEqualTo("2");
//
//    }
//
//    private ChatRoom createChatMessage(Member client, Member center) {
//        ChatRoom chatRoom = ChatRoom.createChatRoom(client, center);
//        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
//
//        ChatMessage chatMessage1 = ChatMessage.createChatMessage(client, savedChatRoom, "1");
//        ChatMessage chatMessage2 = ChatMessage.createChatMessage(client, savedChatRoom, "2");
//        chatMessageRepository.saveAll(List.of(chatMessage1, chatMessage2));
//        return savedChatRoom;
//    }
//}
