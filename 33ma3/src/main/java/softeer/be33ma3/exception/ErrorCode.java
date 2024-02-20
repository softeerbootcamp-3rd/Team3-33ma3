package softeer.be33ma3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //공통
    NOT_FOUND_CENTER(HttpStatus.NOT_FOUND,"존재하지 않는 센터"),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "존재하지 않는 회원"),
    AUTHOR_ONLY_ACCESS(HttpStatus.UNAUTHORIZED ,"작성자만 가능합니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),

    //post
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "존재하지 않는 게시글"),
    POST_CREATION_DISABLED(HttpStatus.UNAUTHORIZED, "센터는 글 작성이 불가능합니다"),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "경매 중인 게시글을 보려면 로그인해주세요"),
    PRE_AUCTION_ONLY(HttpStatus.BAD_REQUEST,"경매 시작 전에만 가능합니다."),

    //위치
    NOT_FOUND_REGION(HttpStatus.NOT_FOUND,"존재하지 않는 구"),
    NO_DISTRICT_IN_ADDRESS(HttpStatus.BAD_REQUEST,"주소에서 구를 찾을 수 없음"),

    //채팅
    ONLY_POST_AUTHOR_ALLOWED(HttpStatus.UNAUTHORIZED, "게시글 작성자만 생성할 수 있습니다."),
    NOT_FOUND_CHAT_ROOM(HttpStatus.NOT_FOUND,"존재하지 않는 채팅 룸"),
    NOT_A_MEMBER_OF_ROOM(HttpStatus.UNAUTHORIZED, "해당 방의 회원이 아닙니다."),

    //회원가입, 로그인
    DUPLICATE_ID(HttpStatus.BAD_REQUEST,"이미 존재하는 아이디"),
    ID_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST,"아이디 또는 비밀번호가 일치하지 않음"),

    //견적
    NOT_FOUND_OFFER(HttpStatus.NOT_FOUND,"존재하지 않는 견적"),
    ALREADY_SUBMITTED(HttpStatus.UNAUTHORIZED, "이미 견적을 작성하였습니다."),
    NOT_CENTER(HttpStatus.UNAUTHORIZED, "센터만 견적을 제시할 수 있습니다."),
    ONLY_LOWER_AMOUNT_ALLOWED(HttpStatus.BAD_REQUEST,"기존 금액보다 낮은 금액으로만 수정 가능합니다."),
    CLOSED_POST(HttpStatus.BAD_REQUEST,"마감된 게시글"),

    //이미지
    UNABLE_TO_CONVERT_FILE(HttpStatus.BAD_REQUEST,"파일을 변환할 수 없음"),

    //JWT
    TOKEN_REQUIRED(HttpStatus.UNAUTHORIZED, "JWT 토큰 필요"),
    JWT_NOT_VALID(HttpStatus.UNAUTHORIZED, "JWT_NOT_VALID"),
    REFRESH_TOKEN_REQUIRED(HttpStatus.UNAUTHORIZED, "REFRESH TOKEN 필요"),
    REFRESH_TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, "올바르지 않은 리프레시 토큰"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰"),

    //review
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰"),
    NOT_DONE_POST(HttpStatus.BAD_REQUEST, "경매가 진행 중입니다."),
    ALREADY_WROTE_REVIEW(HttpStatus.BAD_REQUEST, "이미 리뷰를 작성하였습니다."),
    NO_SELECTED_CENTER(HttpStatus.NOT_FOUND, "낙찰 처리된 센터가 없습니다."),

    //S3
    INVALID_FILE(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일");

    private final HttpStatus status;
    private final String errorMessage;
}
