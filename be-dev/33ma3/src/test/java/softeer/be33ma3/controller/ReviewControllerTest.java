package softeer.be33ma3.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import softeer.be33ma3.service.ReviewService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private ReviewService reviewService;

    @DisplayName("전체 리뷰 조회를 할 수 있다.")
    @Test
    void showAllReview() throws Exception {
        //given //when //then
        mockMvc.perform(MockMvcRequestBuilders.get("/review"))
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
        mockMvc.perform(MockMvcRequestBuilders.get("/review/{center_id}", centerId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("센터 리뷰 조회 성공"));
    }
}
