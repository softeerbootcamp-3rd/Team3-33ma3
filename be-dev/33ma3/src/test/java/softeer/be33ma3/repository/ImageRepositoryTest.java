package softeer.be33ma3.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import softeer.be33ma3.domain.Image;

import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
class ImageRepositoryTest {
    @Autowired private ImageRepository imageRepository;

    @AfterEach
    void tearDown(){
        imageRepository.deleteAllInBatch();
    }

    @DisplayName("이미지 아이디 리스트로 파일 이름을 찾을 수 있다.")
    @Test
    void findFileNamesByImageIdIn(){
        //given
        Image image1 = Image.createImage("aaaaaa", "test1");
        Image image2 = Image.createImage("bbbbbb", "test2");
        Image image3 = Image.createImage("cccccc", "test3");

        List<Image> images = imageRepository.saveAll(List.of(image1, image2, image3));

        List<Long> imageIds = images.stream()
                .map(Image::getImageId)
                .toList();

        //when
        List<String> fileNames = imageRepository.findFileNamesByImageIds(imageIds);

        //then
        Assertions.assertThat(fileNames).hasSize(3)
                .contains("test1", "test2", "test3");
    }
}
