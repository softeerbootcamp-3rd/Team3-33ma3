package softeer.be33ma3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.jwt.JwtProvider;
import softeer.be33ma3.repository.MemberRepository;
import softeer.be33ma3.service.ChatService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ChatControllerTest {
    private static String accessToken;

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JwtProvider jwtProvider;
    @Autowired private MemberRepository memberRepository;
    @MockBean private ChatService chatService;

    @BeforeEach
    void setUp(){
        Member member = Member.createClient("테스트", "1234", null);
        Member savedMember = memberRepository.save(member);
        accessToken = jwtProvider.createAccessToken(savedMember.getMemberType(), savedMember.getMemberId(), savedMember.getLoginId());
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("채팅방을 만들 수 있다.")
    @Test
    void createRoom() throws Exception {
        //given
        Long postId = 1L;
        Long centerId = 2L;

        //when //then
        mockMvc.perform(post("/chatRoom/{post_id}/{center_id}", postId, centerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("채팅방 생성 성공"));
    }

    @DisplayName("기존의 모든 문의 내역을 반환한다.")
    @Test
    void showAllChatRoom() throws Exception {
        //given //when //then
        mockMvc.perform(get("/chatRoom/all")
                        .header("Authorization", accessToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("문의 내역 전송 성공"));
    }

    @DisplayName("기존의 모든 채팅 내역을 반환한다.")
    @Test
    void showOneChatHistory() throws Exception {
        //given
        Long roomId = 1L;

        // when //then
        mockMvc.perform(get("/chat/history/{room_id}", roomId)
                        .header("Authorization", accessToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("채팅 내역 전송 성공"));
    }
}
