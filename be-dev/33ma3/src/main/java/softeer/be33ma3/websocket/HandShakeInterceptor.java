
package softeer.be33ma3.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class HandShakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String[] parts = request.getURI().getPath().split("/");
        if(parts.length != 5) {
            log.error("잘못된 웹소켓 연결 요청");
            response.setStatusCode(HttpStatus.FORBIDDEN);
            response.getBody().write("웹소켓 연결 실패, 엔드포인트를 다시 확인해주세요.".getBytes());
            return false;
        }
        // 게시글 조회 관련 실시간 통신 요청일 경우
        if(parts[2].equals("post")) {
            return attributesToHandler(response, attributes, parts, "postId");
        }
        if(parts[2].equals("chat")) {
            return attributesToHandler(response, attributes, parts, "roomId");
        }

        if(parts[2].equals("chatRoom")) {
            return attributesToHandler(response, attributes, parts, "all");
        }

        return false;
    }

        private boolean attributesToHandler(ServerHttpResponse response, Map<String, Object> attributes, String[] parts, String part3) throws IOException {
            // WebSocketHandler 에 전달될 속성 추가하기
            try {
                attributes.put("type", parts[2]);
                if (!parts[2].equals("chatRoom")) {
                    // 연결 요청 엔드 포인트에서 데이터 파싱
                    attributes.put(part3, Long.parseLong(parts[3]));
                }

                Long memberId = Long.parseLong(parts[4]);
                attributes.put("memberId", memberId);
                return true;
            }catch(NumberFormatException e) {
                log.error("웹소켓 연결 실패: 멤버 아이디가 포함되어야 합니다.");
                response.setStatusCode(HttpStatus.FORBIDDEN);
                response.getBody().write("웹소켓 연결 실패: 멤버 아이디를 포함해주세요.".getBytes());
                return false;
            }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("handshake success!");
    }
}
