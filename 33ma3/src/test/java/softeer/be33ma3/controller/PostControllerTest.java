package softeer.be33ma3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.dto.request.PostCreateDto;
import softeer.be33ma3.jwt.JwtProvider;
import softeer.be33ma3.repository.MemberRepository;
import softeer.be33ma3.service.PostService;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {
    private static String accessToken;

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private PostService postService;
    @Autowired private JwtProvider jwtProvider;
    @Autowired private MemberRepository memberRepository;

    @BeforeEach
    void setUp(){
        Member member = Member.createClient("테스트", "1234", null);
        Member savedMember = memberRepository.save(member);
        accessToken = jwtProvider.createAccessToken(savedMember.getMemberType(), savedMember.getMemberId(), savedMember.getLoginId());
    }
    @DisplayName("게시글을 생성할 수 있다.")
    @Test
    void cratePost() throws Exception {
        //given
        PostCreateDto postCreateDto = new PostCreateDto("승용차", "제네시스", 3, "서울시 강남구", "기스, 깨짐", "오일 교체", new ArrayList<>(),"수정전 내용");
        MockMultipartFile multipartFile = createImages();

        String request = objectMapper.writeValueAsString(postCreateDto);

        //when //then
        mockMvc.perform(multipart("/post/create")
                        .file(new MockMultipartFile("request", "", "application/json", request.getBytes(StandardCharsets.UTF_8)))
                        .file(multipartFile)
                        .contentType(MULTIPART_FORM_DATA)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("게시글 작성 성공"));
    }

    @DisplayName("게시글을 수정할 수 있다.")
    @Test
    void editPost(){
        //given

        //when //then
    }

    @DisplayName("게시글을 삭제할 수 있다.")
    @Test
    void deletePost(){
        //given

        //when //then
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
