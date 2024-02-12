package softeer.be33ma3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softeer.be33ma3.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByLoginId(String loginId);

    Optional<Member> findMemberByRefreshToken(String refreshToken);

    Optional<Member> findByLoginIdAndPassword(String loginId, String password);
}
