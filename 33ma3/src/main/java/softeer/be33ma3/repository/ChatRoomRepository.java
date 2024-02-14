package softeer.be33ma3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softeer.be33ma3.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
