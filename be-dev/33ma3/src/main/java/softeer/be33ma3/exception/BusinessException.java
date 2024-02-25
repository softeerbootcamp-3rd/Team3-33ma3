package softeer.be33ma3.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    protected ErrorCode errorCode;
    public BusinessException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}
