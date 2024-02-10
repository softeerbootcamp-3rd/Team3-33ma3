package softeer.be33ma3.exception;

public class JwtTokenException extends IllegalArgumentException {
    public JwtTokenException(String message) {
        super(message);
    }
}
