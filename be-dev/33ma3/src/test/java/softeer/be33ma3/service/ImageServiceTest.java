package softeer.be33ma3.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import softeer.be33ma3.domain.Image;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.exception.ErrorCode;
import softeer.be33ma3.repository.ImageRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles("test")
class ImageServiceTest {
    @Autowired private ImageService imageService;
    @Autowired private ImageRepository imageRepository;

    @AfterEach
    void tearDown() {
        imageRepository.deleteAllInBatch();
    }

    @DisplayName("이미지를 s3에 업로드 하고, db에 저장할 수 있다.")
    @Test
    void saveImage() throws IOException {
        //given
        MockMultipartFile images = createImages();

        //when
        Image image = imageService.saveImage(images);

        //then
        assertThat(image).isInstanceOf(Image.class);
    }

    @DisplayName("파일 확장자가 이상하면 이미지 저장시 예외가 발생한다.")
    @Test
    void saveImageWithWeirdExtension() throws IOException {
        //given
        MockMultipartFile images = createWeirdImages();

        //when
        assertThatThrownBy(() -> imageService.saveImage(images))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_FILE);
    }

    @DisplayName("이미지 아이디로 이미지를 삭제할 수 있다.")
    @Test
    void deleteImage(){
        //given
        Image image1 = Image.createImage("test1", "test1.png");
        Image savedImage1 = imageRepository.save(image1);
        Image image2 = Image.createImage("test2", "test2.png");
        Image savedImage2 = imageRepository.save(image2);

        List<Image> imageList = new ArrayList<>();
        imageList.add(savedImage1);
        imageList.add(savedImage2);

        //when //then
        assertDoesNotThrow(() -> imageService.deleteImage(imageList));        // 이미지 삭제 메서드를 호출하고 오류가 발생하지 않는지 확인합니다.
    }

    private MockMultipartFile createImages() throws IOException {
        String fileName = "testImage"; //파일명
        String contentType = "jpg"; //파일타입
        String filePath = "src/test/resources/testImage/"+fileName+"."+contentType;

        FileInputStream fileInputStream = new FileInputStream(filePath);
        MockMultipartFile multipartFile = new MockMultipartFile(
                "images",
                fileName + "." + contentType,
                contentType,
                fileInputStream
        );
        return multipartFile;
    }

    private MockMultipartFile createWeirdImages() throws IOException {
        String fileName = "testImage"; //파일명
        String contentType = "jpg"; //파일타입
        String filePath = "src/test/resources/testImage/"+fileName+"."+contentType;

        FileInputStream fileInputStream = new FileInputStream(filePath);
        MockMultipartFile multipartFile = new MockMultipartFile(
                "images",
                fileName  + contentType,
                contentType,
                fileInputStream
        );
        return multipartFile;
    }
}
