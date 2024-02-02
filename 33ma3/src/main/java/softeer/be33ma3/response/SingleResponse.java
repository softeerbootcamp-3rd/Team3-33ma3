package softeer.be33ma3.response;


public class SingleResponse {
    private static final String SUCCESS_STATUS = "SUCCESS";
    private static final String ERROR_STATUS = "ERROR";
    private String status;
    private String messsage;

    public SingleResponse(String status, String messsage) {
        this.status = status;
        this.messsage = messsage;
    }
    public static SingleResponse success(String messsage){
        return new SingleResponse(SUCCESS_STATUS, messsage);
    }
    public static SingleResponse error(String messsage){
        return new SingleResponse(ERROR_STATUS, messsage);
    }
}
