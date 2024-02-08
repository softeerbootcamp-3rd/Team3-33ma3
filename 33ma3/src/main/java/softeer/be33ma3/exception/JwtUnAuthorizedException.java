package softeer.be33ma3.exception;

public class JwtUnAuthorizedException extends IllegalArgumentException { // 401 에러
    public JwtUnAuthorizedException(String message) {
        super(message);
    }
}
