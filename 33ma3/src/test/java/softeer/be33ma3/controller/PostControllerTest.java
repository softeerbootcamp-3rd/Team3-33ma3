//package softeer.be33ma3.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import softeer.be33ma3.dto.request.PostCreateDto;
//import softeer.be33ma3.service.PostService;
//
//import java.util.Arrays;
//
//@SpringBootTest
//@AutoConfigureMockMvc   //@WebMvcTest 없이도 MockMvc를 자동으로 설정
//class PostControllerTest {
//    @Autowired private MockMvc mockMvc;
//    @MockBean private PostService postService;
//    @Autowired private ObjectMapper objectMapper;   //객체와 JSON 데이터 간의 변환을 처리
//
//    @DisplayName("게시글 직성을 성공하면 200 Response가 반환된다.")
//    @Test
//    void createPost() throws Exception {
//        //given
//        PostCreateDto request = new PostCreateDto(1L, "승용차", "제네시스", 3,
//                "서울시 강남구 10Km 반경", "깨짐, 기스", "오일 교체", Arrays.asList(), Arrays.asList(), "댓글 댓글");
//
//        //when //then
//        mockMvc.perform(MockMvcRequestBuilders.post("/post/create")
//                        .content(objectMapper.writeValueAsString(request))
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(MockMvcResultHandlers.print())   //자세한 로그를 볼 수 있음
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("게시글 작성 성공"));
//    }
//
//    @DisplayName("게시글 작성시 필드가 blank면 예외가 발생한다.")
//    @Test
//    void createPostWithoutCarType() throws Exception {
//        //given
//        PostCreateDto request = new PostCreateDto(1L, "", "제네시스", 3,
//                "서울시 강남구 10Km 반경", "깨짐, 기스", "오일 교체", Arrays.asList(), Arrays.asList(), "댓글 댓글");
//
//        //when //then
//        mockMvc.perform(MockMvcRequestBuilders.post("/post/create")
//                        .content(objectMapper.writeValueAsString(request))
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(MockMvcResultHandlers.print())   //자세한 로그를 볼 수 있음
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("ERROR"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("차종은 필수입니다."));
//    }
//}
