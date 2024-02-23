package softeer.be33ma3.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import softeer.be33ma3.service.LocationService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc   //@WebMvcTest 없이도 MockMvc를 자동으로 설정
class LocationControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private LocationService locationService;

    @DisplayName("사용자 위치에서 반경안에 있는 서비스 센터들을 반환한다.")
    @Test
    void getCentersInRadius() throws Exception {
        //given
        double latitude = 37.547427;
        double longitude = 127.056600;
        double radius = 1.0;

        //when //then
        mockMvc.perform(get("/location?latitude=" + latitude + "&longitude=" + longitude + "&radius=" + radius))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("반경 내 위치한 센터 정보 전송 완료"));
    }

    @DisplayName("회원가입 된 모든 서비스 센터들을 반환한다.")
    @Test
    void getAllCenters() throws Exception {
        //given //when //then
        mockMvc.perform(get("/center/all"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("모든 센터 정보 전송 완료"));
    }
}
