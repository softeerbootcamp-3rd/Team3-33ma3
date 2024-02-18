package softeer.be33ma3.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import softeer.be33ma3.response.SingleResponse;


@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {
    private final MessageSource ms;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handlerBizException(BusinessException e) {;
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(SingleResponse.error(e.getErrorCode().getErrorMessage()));
    }
}
