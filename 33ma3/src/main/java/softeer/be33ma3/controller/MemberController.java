package softeer.be33ma3.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import softeer.be33ma3.dto.request.MemberLoginDto;
import softeer.be33ma3.dto.request.MemberSignUpDto;
import softeer.be33ma3.response.DataResponse;
import softeer.be33ma3.response.SingleResponse;
import softeer.be33ma3.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member/signUp")
    public ResponseEntity<?> signUp(@RequestBody @Valid MemberSignUpDto memberSignUpDto){
        memberService.memberSignUp(memberSignUpDto);

        return ResponseEntity.ok(SingleResponse.success("회원가입 성공"));
    }

    @PostMapping("/member/login")
    public ResponseEntity<?> login(@RequestBody @Valid MemberLoginDto memberLoginDto){
        String jwtToken = memberService.login(memberLoginDto);

        return ResponseEntity.ok(DataResponse.success("로그인 성공", jwtToken));
    }
}
