package softeer.be33ma3.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import softeer.be33ma3.service.ImageService;

import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@SpringBootTest
@AutoConfigureMockMvc
class ImageControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private ImageService imageService;

    @DisplayName("여러장의 이미지들을 저장할 수 있다.")
    @Test
    void saveImages() throws Exception {
        //given
        final String fileName = "testImage"; //파일명
        final String contentType = "jpg"; //파일타입
        final String filePath = "src/test/resources/testImage/"+fileName+"."+contentType; //파일경로
        FileInputStream fileInputStream = new FileInputStream(filePath);

        //Mock파일생성
        MockMultipartFile image = new MockMultipartFile(
                "images",
                fileName + "." + contentType, //originalFilename
                contentType,
                fileInputStream
        );

        //when //then
        mockMvc.perform(
                multipart("/post/image").file(image))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("이미지 저장 성공"));
    }
}
