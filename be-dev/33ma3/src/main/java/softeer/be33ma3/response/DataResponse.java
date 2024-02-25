package softeer.be33ma3.response;

import lombok.Getter;

@Getter
public class DataResponse<T> extends SingleResponse{
    private T data;

    public DataResponse(String status, String message, T data) {
        super(status, message);
        this.data = data;
    }

    public static <T> DataResponse<T> success(String message, T data){
        return new DataResponse<>(SUCCESS_STATUS, message, data);
    }
}
