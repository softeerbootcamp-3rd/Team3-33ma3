package softeer.be33ma3.response;

public class DataResponse<T> extends SingleResponse{
    private static final String SUCCESS_STATUS = "SUCCESS";
    private T data;

    public DataResponse(String status, String message, T data) {
        super(status, message);
        this.data = data;
    }
    public static <T> DataResponse<T> success(String message, T data){
        return new DataResponse<>(SUCCESS_STATUS, message, data);
    }
}
