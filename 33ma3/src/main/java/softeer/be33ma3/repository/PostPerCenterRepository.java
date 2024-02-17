package softeer.be33ma3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import softeer.be33ma3.domain.Post;
import softeer.be33ma3.domain.PostPerCenter;

import java.util.List;

public interface PostPerCenterRepository extends JpaRepository<PostPerCenter, Long> {
    @Query("SELECT ppc.post FROM PostPerCenter ppc WHERE ppc.center.centerId = :centerId ORDER BY ppc.post.createTime DESC")
    List<Post> findPostsByCenter_CenterIdOrderByCreateTimeDesc(Long centerId);
}
