package softeer.be33ma3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import softeer.be33ma3.domain.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageCustomRepository{
    @Query("SELECT COALESCE(COUNT(c), 0) FROM ChatMessage c WHERE c.chatRoom.chatRoomId = :chatRoomId AND c.readDone = false AND c.sender.memberId != :memberId")
    long countReadDoneIsFalse(@Param("chatRoomId") Long chatRoomId, @Param("memberId") Long memberId);  //내가 아닌 다른 사람이 보낸 경우 읽음이 false 인 개수

    List<ChatMessage> findByChatRoom_ChatRoomId(Long roomId);
}
