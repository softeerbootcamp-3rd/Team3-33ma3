package softeer.be33ma3.response;

import lombok.Getter;

@Getter
public class SingleResponse {
    protected static final String SUCCESS_STATUS = "SUCCESS";
    private static final String ERROR_STATUS = "ERROR";
    private String status;
    private String message;

    public SingleResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public static SingleResponse success(String message){
        return new SingleResponse(SUCCESS_STATUS, message);
    }
    public static SingleResponse error(String message){
        return new SingleResponse(ERROR_STATUS, message);
    }
}
