package softeer.be33ma3.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import softeer.be33ma3.dto.request.CenterSignUpDto;
import softeer.be33ma3.dto.request.ClientSignUpDto;
import softeer.be33ma3.service.MemberService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private MemberService memberService;


    @DisplayName("일반 사용자 회원가입")
    @Test
    void clientSignUp() throws Exception {
        //given
        ClientSignUpDto request = new ClientSignUpDto("client1", "1234");

        //when //then
        mockMvc.perform(post("/client/signUp")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //자세한 로그 볼 수 있음
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("회원가입 성공"));
    }

    @DisplayName("패스워드가 비어있으면 예외가 발생한다.")
    @Test
    void clientSignUpWithOutPassword() throws Exception {
        //given
        ClientSignUpDto request = new ClientSignUpDto("client1", "");

        //when //then
        mockMvc.perform(post("/client/signUp")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())   //자세한 로그를 볼 수 있음
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("비밀번호는 필수입니다."));
    }

    @DisplayName("센터 회원가입")
    @Test
    void centerSignUp() throws Exception {
        //given
        CenterSignUpDto request = new CenterSignUpDto("center", "1234", "테스트 센터 강남점" ,37.1234, 127.1234);

        //when //then
        mockMvc.perform(post("/center/signUp")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //자세한 로그 볼 수 있음
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("회원가입 성공"));
    }

    @DisplayName("센터 이름이 비어있는 경우 예외가 발생한다.")
    @Test
    void centerSignUpWithoutCenterName() throws Exception{
        //given
        CenterSignUpDto request = new CenterSignUpDto("center", "1234", "" ,37.1234, 127.1234);

        //when //then
        mockMvc.perform(post("/center/signUp")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())   //자세한 로그를 볼 수 있음
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("센터이름은 필수입니다."));
    }
}
