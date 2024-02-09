package softeer.be33ma3.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softeer.be33ma3.dto.request.MemberLoginDto;
import softeer.be33ma3.dto.request.MemberSignUpDto;
import softeer.be33ma3.jwt.JwtService;
import softeer.be33ma3.jwt.JwtToken;
import softeer.be33ma3.response.DataResponse;
import softeer.be33ma3.response.SingleResponse;
import softeer.be33ma3.service.MemberService;

import static softeer.be33ma3.jwt.JwtProperties.REFRESH_HEADER_STRING;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final JwtService jwtService;

    @PostMapping("/member/signUp")
    public ResponseEntity<?> signUp(@RequestBody @Valid MemberSignUpDto memberSignUpDto){
        memberService.memberSignUp(memberSignUpDto);

        return ResponseEntity.ok(SingleResponse.success("회원가입 성공"));
    }

    @PostMapping("/member/login")
    public ResponseEntity<?> login(@RequestBody @Valid MemberLoginDto memberLoginDto){
        JwtToken jwtToken = memberService.login(memberLoginDto);

        return ResponseEntity.ok(DataResponse.success("로그인 성공", jwtToken));
    }

    //refreshToken 으로 accessToken 재발급
    @PostMapping("/reissueToken")
    public ResponseEntity<?> reissueToken(@RequestHeader(REFRESH_HEADER_STRING) String refreshToken){
        String accessToken = jwtService.reissue(refreshToken);

        return ResponseEntity.ok(DataResponse.success("토큰 재발급 성공", accessToken));
    }
}
