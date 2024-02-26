package softeer.be33ma3.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.dto.request.CenterSignUpDto;
import softeer.be33ma3.dto.request.ClientSignUpDto;
import softeer.be33ma3.dto.request.LoginDto;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.exception.ErrorCode;
import softeer.be33ma3.repository.CenterRepository;
import softeer.be33ma3.repository.MemberRepository;

import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class MemberServiceTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;
    @Autowired private CenterRepository centerRepository;

    @BeforeEach
    void setUp(){
        Member client1 = Member.createClient("client1", "1234", null);
        memberRepository.save(client1);
    }

    @AfterEach
    void tearDown(){
        centerRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("일반 사용자 회원가입")
    @Test
    void clientSignUp() throws IOException {
        //given
        MockMultipartFile profile = createImages();
        ClientSignUpDto clientSignUpDto = new ClientSignUpDto("test1", "1234");

        //when
        memberService.clientSignUp(clientSignUpDto, profile);

        //then
        Member member = memberRepository.findByLoginIdAndPassword("test1", "1234").get();
        assertThat(member)
                .extracting("loginId", "password")
                .containsExactly("test1", "1234");
    }

    @DisplayName("센터 회원가입")
    @Test
    void centerSignUp() throws IOException {
        //given
        MockMultipartFile profile = createImages();
        CenterSignUpDto centerSignUpDto = new CenterSignUpDto("test1", "1234", 37.5, 127.0);

        //when
        memberService.clientSignUp(centerSignUpDto, profile);

        //then
        Member member = memberRepository.findByLoginIdAndPassword("test1", "1234").get();
        assertThat(member)
                .extracting("loginId", "password")
                .containsExactly("test1", "1234");
    }

    @DisplayName("센터와 일반 사용자 로그인 기능")
    @Test
    void login(){
        //given
        LoginDto loginDto = new LoginDto("client1", "1234");

        //when
        memberService.login(loginDto);

        //then
        Member member = memberRepository.findByLoginIdAndPassword("client1", "1234").get();
        assertThat(member.getRefreshToken()).isNotNull();
    }

    @DisplayName("아이디 또는 비밀번호가 다르면 예외가 발생한다.")
    @Test
    void loginWithWrongId(){
        //given
        LoginDto loginDto = new LoginDto("client2", "1234");

        //when //then
        assertThatThrownBy(() -> memberService.login(loginDto))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ID_PASSWORD_MISMATCH);
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
