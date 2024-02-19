package softeer.be33ma3.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import softeer.be33ma3.domain.ChatRoom;
import softeer.be33ma3.domain.Member;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
class ChatRoomRepositoryTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private ChatRoomRepository chatRoomRepository;

    @AfterEach
    void tearDown() {
        chatRoomRepository.deleteAllInBatch();
        chatRoomRepository.deleteAllInBatch();
    }

    @DisplayName("센터아이디와 클라이언트 아이디로 두 사용자의 채팅방을 찾을 수 있다.")
    @Test
    void indRoomIdByCenterIdAndClientId(){
        //given
        Member client = new Member(1, "client1", "1234");
        client = memberRepository.save(client);
        Member center = new Member(2, "center1", "1234");
        center = memberRepository.save(center);

        ChatRoom chatRoom = ChatRoom.createChatRoom(client, center);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        //when
        Long roomId = chatRoomRepository.findRoomIdByCenterIdAndClientId(center.getMemberId(), client.getMemberId()).get();

        //then
        assertThat(roomId).isEqualTo(savedChatRoom.getChatRoomId());
    }
}
