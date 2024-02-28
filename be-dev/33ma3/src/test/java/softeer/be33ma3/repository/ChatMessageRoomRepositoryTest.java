package softeer.be33ma3.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import softeer.be33ma3.domain.ChatRoom;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.repository.ChatRoomRepository;
import softeer.be33ma3.repository.MemberRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
class ChatMessageRoomRepositoryTest {
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
        chatRoomRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("센터아이디와 클라이언트 아이디로 두 사용자의 채팅방을 찾을 수 있다.")
    @Test
    void findRoomIdByCenterIdAndClientId(){
        //given
        Member client = memberRepository.findMemberByLoginId("client1").get();
        Member center = memberRepository.findMemberByLoginId("center1").get();

        ChatRoom chatRoom = ChatRoom.createChatRoom(client, center);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        //when
        Long roomId = chatRoomRepository.findRoomIdByCenterIdAndClientId(center.getMemberId(), client.getMemberId()).get();

        //then
        assertThat(roomId).isEqualTo(savedChatRoom.getChatRoomId());
    }

    @DisplayName("센터 아이디로 해당 센터가 가지고 있는 채팅방들을 찾을 수 있다.")
    @Test
    void findByCenter_MemberId(){
        //given
        Member client = memberRepository.findMemberByLoginId("client1").get();
        Member center = memberRepository.findMemberByLoginId("center1").get();

        ChatRoom chatRoom1 = ChatRoom.createChatRoom(client, center);
        ChatRoom chatRoom2 = ChatRoom.createChatRoom(client, center);

        chatRoomRepository.saveAll(List.of(chatRoom1, chatRoom2));

        //when
        List<ChatRoom> chatRooms = chatRoomRepository.findByCenter_MemberId(center.getMemberId());

        //then
        assertThat(chatRooms).hasSize(2);
    }

    @DisplayName("클라이언트 아이디로 해당 클라이언트가 가지고 있는 채팅방들을 찾을 수 있디.")
    @Test
    void findByClient_MemberId(){
        //given
        Member client = memberRepository.findMemberByLoginId("client1").get();
        Member center = memberRepository.findMemberByLoginId("center1").get();

        ChatRoom chatRoom1 = ChatRoom.createChatRoom(client, center);
        ChatRoom chatRoom2 = ChatRoom.createChatRoom(client, center);

        chatRoomRepository.saveAll(List.of(chatRoom1, chatRoom2));

        //when
        List<ChatRoom> chatRooms = chatRoomRepository.findByClient_MemberId(client.getMemberId());

        //then
        assertThat(chatRooms).hasSize(2);
    }
}
