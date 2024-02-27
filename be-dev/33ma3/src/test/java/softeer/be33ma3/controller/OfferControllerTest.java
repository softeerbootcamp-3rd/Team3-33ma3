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
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.dto.request.OfferCreateDto;
import softeer.be33ma3.dto.response.OfferDetailDto;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.jwt.JwtProvider;
import softeer.be33ma3.repository.MemberRepository;
import softeer.be33ma3.service.OfferService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static softeer.be33ma3.exception.ErrorCode.NOT_FOUND_OFFER;
import static softeer.be33ma3.exception.ErrorCode.NOT_FOUND_POST;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class OfferControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private OfferService offerService;
    @Autowired private JwtProvider jwtProvider;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MemberRepository memberRepository;
    private static String accessToken;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("해당하는 댓글 세부사항을 반환한다.")
    void showOffer() throws Exception {
        // given
        saveLoginClient("user1", "user1");
        OfferDetailDto mockOfferDetailDto = OfferDetailDto.builder().offerId(1L).build();
        when(offerService.showOffer(eq(1L), eq(1L))).thenReturn(mockOfferDetailDto);
        // when & then
        mockMvc.perform(get("/post/{post_id}/offer/{offer_id}", 1L, 1L)
                        .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("견적 불러오기 성공"))
                .andExpect(jsonPath("$.data.offerId").value(mockOfferDetailDto.getOfferId()));
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 대해 댓글 조회 요청이 올 경우 404 응답이 반환된다.")
    void showOffer_withNoPost() throws Exception {
        // given
        saveLoginClient("user1", "user1");
        when(offerService.showOffer(eq(1L), eq(1L))).thenThrow(new BusinessException(NOT_FOUND_POST));
        // when & then
        mockMvc.perform(get("/post/{post_id}/offer/{offer_id}", 1L, 1L)
                        .header("Authorization", accessToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 게시글"));
    }

    @Test
    @DisplayName("존재하지 않는 댓글에 대해 댓글 조회 요청이 올 경우 404 응답이 반환된다.")
    void showOffer_withNoOffer() throws Exception {
        // given
        saveLoginClient("user1", "user1");
        when(offerService.showOffer(eq(1L), eq(1L))).thenThrow(new BusinessException(NOT_FOUND_OFFER));
        // when & then
        mockMvc.perform(get("/post/{post_id}/offer/{offer_id}", 1L, 1L)
                        .header("Authorization", accessToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 견적"));
    }

    @Test
    @DisplayName("해당하는 게시글에 댓글을 생성한다.")
    void createOffer() throws Exception {
        // given
        OfferCreateDto offerCreateDto = new OfferCreateDto(10, "offer"); // 생성에 필요한 DTO 객체 생성
        Member member = saveLoginClient("user1", "user1");
        Long mockOfferId = 1L; // 모의 offerId
        when(offerService.createOffer(eq(1L), eq(offerCreateDto), eq(member))).thenReturn(mockOfferId);

        // when & then
        mockMvc.perform(post("/post/{post_id}/offer", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offerCreateDto)) // DTO 객체를 JSON 문자열로 변환하여 요청 본문에 추가
                        .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("입찰 성공"));
    }

    @Test
    @DisplayName("해당 댓글을 수정할 수 있다.")
    void updateOffer() throws Exception {
        // given
        OfferCreateDto offerCreateDto = new OfferCreateDto(10, "offer"); // 수정에 필요한 DTO 객체 생성
        Member member = saveLoginClient("user1", "user1");

        // when & then
        mockMvc.perform(patch("/post/{post_id}/offer/{offer_id}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offerCreateDto)) // DTO 객체를 JSON 문자열로 변환하여 요청 본문에 추가
                        .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("댓글 수정 성공"));
    }

    @Test
    @DisplayName("해당 댓글을 삭제할 수 있다.")
    void deleteOffer() throws Exception {
        // given
        Member member = saveLoginClient("user1", "user1");

        // when & then
        mockMvc.perform(delete("/post/{post_id}/offer/{offer_id}", 1L, 1L)
                        .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("댓글 삭제 성공"));
    }

    @Test
    @DisplayName("해당 댓글을 낙찰할 수 있다.")
    void selectOffer() throws Exception {
        // given
        Member member = saveLoginClient("user1", "user1");

        // when & then
        mockMvc.perform(get("/post/{post_id}/offer/{offer_id}/select", 1L, 1L)
                        .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("낙찰 완료, 게시글 마감"));
    }

    private Member saveLoginClient(String loginId, String password) {
        Member client = Member.createClient(loginId, password, null);
        Member savedClient = memberRepository.save(client);
        accessToken = jwtProvider.createAccessToken(savedClient.getMemberType(), savedClient.getMemberId(), savedClient.getLoginId());
        return savedClient;
    }
}
