package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    // TODO: 현재 로그인한 유저의 정보를 이용하여 member id 가져오기
    public static Long getMemberId() {
        return 1L;
    }
}
