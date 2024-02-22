package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import softeer.be33ma3.domain.Center;
import softeer.be33ma3.domain.Image;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.dto.request.CenterSignUpDto;
import softeer.be33ma3.dto.request.LoginDto;
import softeer.be33ma3.dto.request.ClientSignUpDto;
import softeer.be33ma3.dto.response.LoginSuccessDto;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.jwt.JwtService;
import softeer.be33ma3.jwt.JwtToken;
import softeer.be33ma3.repository.CenterRepository;
import softeer.be33ma3.repository.ImageRepository;
import softeer.be33ma3.repository.MemberRepository;

import static softeer.be33ma3.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final CenterRepository centerRepository;
    private final JwtService jwtService;
    private final ImageService imageService;
    private final S3Service s3Service;
    private final ImageRepository imageRepository;

    @Transactional
    public Long clientSignUp(ClientSignUpDto clientSignUpDto) {
        if (memberRepository.findMemberByLoginId(clientSignUpDto.getLoginId()).isPresent()) {//아이디가 이미 존재하는 경우
            throw new BusinessException(DUPLICATE_ID);
        }

        Member member = Member.createClient(clientSignUpDto.getLoginId(), clientSignUpDto.getPassword());
        return memberRepository.save(member).getMemberId();
    }

    @Transactional
    public Long centerSignUp(CenterSignUpDto centerSignUpDto) {
        if (memberRepository.findMemberByLoginId(centerSignUpDto.getLoginId()).isPresent()) {
            throw new BusinessException(DUPLICATE_ID);
        }

        Member member = Member.createCenter(centerSignUpDto.getLoginId(), centerSignUpDto.getPassword());
        Member savedMember = memberRepository.save(member);

        Center center = Center.createCenter(centerSignUpDto.getLatitude(), centerSignUpDto.getLongitude(), savedMember);
        centerRepository.save(center);

        return savedMember.getMemberId();
    }
    @Transactional
    public void addProfile(Long memberId, MultipartFile profile) {
        // 해당하는 유저 가져오기
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER));

        if(profile.isEmpty()) {
            if(member.isCenter()){
                Image image = Image.createImage(s3Service.getFileUrl("profile.png"), "profile.png");
                member.setProfile(imageRepository.save(image));
            }
            //TODO: 일반 회원 전용 프로필 이미지 생기면 저장하는 로직 추가하기
            return;
        }
        // 이미지 저장하기
        Image image = imageService.saveImage(profile);
        member.setProfile(image);
    }

    @Transactional
    public LoginSuccessDto login(LoginDto loginDto) {
        Member member = memberRepository.findByLoginIdAndPassword(loginDto.getLoginId(), loginDto.getPassword())
                .orElseThrow(() -> new BusinessException(ID_PASSWORD_MISMATCH));

        JwtToken jwtToken = jwtService.getJwtToken(member.getMemberType(), member.getMemberId(), loginDto.getLoginId());
        member.setRefreshToken(jwtToken.getRefreshToken()); //리프레시 토큰 저장

        return LoginSuccessDto.createLoginSuccessDto(member, jwtToken);
    }
}
