package softeer.be33ma3.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import softeer.be33ma3.dto.request.CenterSignUpDto;
import softeer.be33ma3.dto.request.ClientSignUpDto;
import softeer.be33ma3.dto.request.LoginDto;
import softeer.be33ma3.jwt.JwtService;
import softeer.be33ma3.service.MemberService;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private MemberService memberService;
    @MockBean private JwtService jwtService;

    @DisplayName("일반 사용자 회원가입")
    @Test
    void clientSignUp() throws Exception {
        //given
        ClientSignUpDto clientSignUpDto = new ClientSignUpDto("test", "1234");
        String request = objectMapper.writeValueAsString(clientSignUpDto);
        MockMultipartFile profile = createImages();

        //when //then
        mockMvc.perform(multipart("/client/signUp")
                        .file(new MockMultipartFile("request", "", "application/json", request.getBytes(StandardCharsets.UTF_8)))
                        .file(profile)
                        .contentType(MULTIPART_FORM_DATA)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("회원가입 성공"));
    }

    @DisplayName("이미지가 없어도 회원가입을 할 수 있다.")
    @Test
    void clientSignUpWithOutImage() throws Exception {
        //given
        ClientSignUpDto clientSignUpDto = new ClientSignUpDto("test", "1234");
        String request = objectMapper.writeValueAsString(clientSignUpDto);

        //when //then
        mockMvc.perform(multipart("/client/signUp")
                        .file(new MockMultipartFile("request", "", "application/json", request.getBytes(StandardCharsets.UTF_8)))
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("회원가입 성공"));
    }

    @DisplayName("센터 회원가입")
    @Test
    void centerSignUp() throws Exception {
        //given
        CenterSignUpDto centerSignUpDto = new CenterSignUpDto("test", "1234", 37.5, 127.0);
        String request = objectMapper.writeValueAsString(centerSignUpDto);

        //when //then
        mockMvc.perform(multipart("/center/signUp")
                        .file(new MockMultipartFile("request", "", "application/json", request.getBytes(StandardCharsets.UTF_8)))
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("회원가입 성공"));
    }

    @DisplayName("센터, 일반 사용자 로그인 기능")
    @Test
    void login() throws Exception {
        //given
        LoginDto request = new LoginDto("client1", "1234");

        //when //then
        mockMvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("로그인 성공"));

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
}
