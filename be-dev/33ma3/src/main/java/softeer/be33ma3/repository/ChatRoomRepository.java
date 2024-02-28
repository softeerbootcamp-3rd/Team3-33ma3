package softeer.be33ma3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import softeer.be33ma3.domain.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByCenter_MemberId(Long memberId);

    List<ChatRoom> findByClient_MemberId(Long memberId);

    @Query(value = "SELECT cr.chatRoomId FROM ChatRoom cr WHERE cr.client.memberId = :clientId AND cr.center.memberId = :centerId")
    Optional<Long> findRoomIdByCenterIdAndClientId(@Param("centerId") Long centerId, @Param("clientId") Long clientId);
}
