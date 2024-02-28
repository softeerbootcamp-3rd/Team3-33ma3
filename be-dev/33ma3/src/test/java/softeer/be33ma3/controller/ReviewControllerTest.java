package softeer.be33ma3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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
import softeer.be33ma3.dto.request.ReviewCreateDto;
import softeer.be33ma3.jwt.JwtProvider;
import softeer.be33ma3.repository.MemberRepository;
import softeer.be33ma3.service.ReviewService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private ReviewService reviewService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private JwtProvider jwtProvider;
    @Autowired private ObjectMapper objectMapper;
    private static String accessToken;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("전체 리뷰 조회를 할 수 있다.")
    @Test
    void showAllReview() throws Exception {
        //given //when //then
        mockMvc.perform(get("/review"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("전체 리뷰 조회 성공"));
    }

    @DisplayName("특정 센터에 대한 리뷰만 확인할 수 있다.")
    @Test
    void showOneCenterReview() throws Exception {
        //given
        Long centerId = 1L;

        //when //then
        mockMvc.perform(get("/review/{center_id}", centerId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("센터 리뷰 조회 성공"));
    }

    @Test
    @DisplayName("성공적으로 리뷰를 작성할 수 있다.")
    void createReview() throws Exception {
        // given
        ReviewCreateDto reviewCreateDto = new ReviewCreateDto(1.5, "review");
        saveLoginClient("user1", "user1");
        // when & then
        mockMvc.perform(post("/review/{post_id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewCreateDto)) // DTO 객체를 JSON 문자열로 변환하여 요청 본문에 추가
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("센터 리뷰 작성 성공"));
    }

    @Test
    @DisplayName("별점을 0점보다 작게 하여 리뷰 작성 요청 시 400 응답이 반환된다.")
    void createReview_withSmallScore() throws Exception{
        // given
        ReviewCreateDto reviewCreateDto = new ReviewCreateDto(-0.999, "review");
        saveLoginClient("user1", "user1");
        // when & then
        mockMvc.perform(post("/review/{post_id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewCreateDto))
                .header("Authorization", accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("별점은 0점 이상이어야 합니다."));
    }

    @Test
    @DisplayName("별점을 딱 0점으로 할 경우 성공적으로 리뷰를 작성할 수 있다.")
    void createReview_withScore0() throws Exception{
        // given
        ReviewCreateDto reviewCreateDto = new ReviewCreateDto(0, "review");
        saveLoginClient("user1", "user1");
        // when & then
        mockMvc.perform(post("/review/{post_id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewCreateDto)) // DTO 객체를 JSON 문자열로 변환하여 요청 본문에 추가
                        .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("센터 리뷰 작성 성공"));
    }

    @Test
    @DisplayName("별점을 딱 5점으로 할 경우 성공적으로 리뷰를 작성할 수 있다.")
    void createReview_withScore5() throws Exception{
        // given
        ReviewCreateDto reviewCreateDto = new ReviewCreateDto(5, "review");
        saveLoginClient("user1", "user1");
        // when & then
        mockMvc.perform(post("/review/{post_id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewCreateDto)) // DTO 객체를 JSON 문자열로 변환하여 요청 본문에 추가
                        .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("센터 리뷰 작성 성공"));
    }

    @Test
    @DisplayName("별점을 5점보다 크게 하여 리뷰 작성 요청 시 400 응답이 반환된다.")
    void createReview_withBigScore() throws Exception{
        // given
        ReviewCreateDto reviewCreateDto = new ReviewCreateDto(5.001, "review");
        saveLoginClient("user1", "user1");
        // when & then
        mockMvc.perform(post("/review/{post_id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewCreateDto))
                        .header("Authorization", accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("별점은 5점 이하여야 합니다."));
    }

    @Test
    @DisplayName("성공적으로 리뷰를 삭제할 수 있다.")
    void deleteReview() throws Exception {
        // given
        Member member = saveLoginClient("user1", "user1");
        // when & then
        mockMvc.perform(delete("/review/{review_id}", 1L)
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("센터 리뷰 삭제 성공"));
    }

    private Member saveLoginClient(String loginId, String password) {
        Member client = Member.createClient(loginId, password, null);
        Member savedClient = memberRepository.save(client);
        accessToken = jwtProvider.createAccessToken(savedClient.getMemberType(), savedClient.getMemberId(), savedClient.getLoginId());
        return savedClient;
    }
}
