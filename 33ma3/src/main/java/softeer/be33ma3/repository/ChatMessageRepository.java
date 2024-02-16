package softeer.be33ma3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import softeer.be33ma3.domain.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT COALESCE(COUNT(c), 0) FROM ChatMessage c WHERE c.chatRoom.chatRoomId = :chatRoomId AND c.readDone = false")
    long countReadDoneIsFalse(@Param("chatRoomId") Long chatRoomId);

    @Query(value = "SELECT c.contents FROM ChatMessage c WHERE c.chatRoom.chatRoomId = :chatRoomId ORDER BY c.createTime DESC LIMIT 1")
    String findTop1ByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}
