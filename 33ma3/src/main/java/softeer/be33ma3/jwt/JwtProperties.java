package softeer.be33ma3.jwt;

public interface JwtProperties {
    long ACCESS_TOKEN_TIME = 1000 * 60 * 33; //33분
    long REFRESH_TOKEN_TIME = 1000 * 60 * 60 * 24 * 13;  //13일
    String ACCESS_PREFIX_STRING = "Bearer ";
    String ACCESS_HEADER_STRING = "Authorization";
    String REFRESH_HEADER_STRING = "Authorization-refresh";
}
