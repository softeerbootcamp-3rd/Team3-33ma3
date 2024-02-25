package softeer.be33ma3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import softeer.be33ma3.domain.Image;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("SELECT i.fileName FROM Image i WHERE i.imageId IN :imageIds")
    List<String> findFileNamesByImageIds(@Param("imageIds") List<Long> imageIds);
}
