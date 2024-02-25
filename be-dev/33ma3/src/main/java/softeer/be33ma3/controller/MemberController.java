package softeer.be33ma3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import softeer.be33ma3.dto.request.CenterSignUpDto;
import softeer.be33ma3.dto.request.LoginDto;
import softeer.be33ma3.dto.request.ClientSignUpDto;
import softeer.be33ma3.dto.response.LoginSuccessDto;
import softeer.be33ma3.jwt.JwtService;
import softeer.be33ma3.response.DataResponse;
import softeer.be33ma3.response.SingleResponse;
import softeer.be33ma3.service.MemberService;

import static softeer.be33ma3.jwt.JwtProperties.REFRESH_HEADER_STRING;

@Tag(name = "Member", description = "회원가입, 로그인, 토큰 관련 api")
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final JwtService jwtService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 아이디", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Operation(summary = "일반 사용자 회원가입", description = "사용자 회원가입 메서드 입니다.")
    @PostMapping(value = "/client/signUp", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> clientSignUp(@RequestPart(name = "request") @Valid ClientSignUpDto clientSignUpDto,
                                          @RequestPart(name = "profile", required = false) MultipartFile profile){
        memberService.clientSignUp(clientSignUpDto, profile);
        return ResponseEntity.ok(SingleResponse.success("회원가입 성공"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 아이디", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Operation(summary = "센터 회원가입", description = "센터 회원가입 메서드 입니다.")
    @PostMapping(value = "/center/signUp", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> centerSignUp(@RequestPart(name = "request") @Valid CenterSignUpDto centerSignUpDto,
                                          @RequestPart(name = "profile", required = false) MultipartFile profile){
        memberService.centerSignUp(centerSignUpDto, profile);
        return ResponseEntity.ok(SingleResponse.success("회원가입 성공"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "400", description = "아이디 또는 비밀번호가 일치하지 않음", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Operation(summary = "로그인", description = "로그인 메서드 입니다.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto){
        LoginSuccessDto loginSuccessDto = memberService.login(loginDto);

        return ResponseEntity.ok(DataResponse.success("로그인 성공", loginSuccessDto));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공", content = @Content(schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Operation(summary = "엑세스 토큰 재발급", description = "엑세스 토큰 재발급 메서드 입니다.")
    @PostMapping("/reissueToken")   //refreshToken 으로 accessToken 재발급
    public ResponseEntity<?> reissueToken(@RequestHeader(REFRESH_HEADER_STRING) String refreshToken){
        String accessToken = jwtService.reissue(refreshToken);

        return ResponseEntity.ok(DataResponse.success("토큰 재발급 성공", accessToken));
    }
}
