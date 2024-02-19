package softeer.be33ma3.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class WebSocketRepository {
    private final Map<Long, Set<Long>> postRoom = new ConcurrentHashMap<>();    // postId : set of memberId
    private final Map<Long, Set<Long>> chatRoom = new ConcurrentHashMap<>();    // roomId : set of memberId
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();     // memberId : WebSocketSession
    private final Map<Long, WebSocketSession> chatRoomListSessions = new ConcurrentHashMap<>();      //memberId: WebSocketSession

    public Set<Long> findAllMemberInPost(Long postId) {
        return postRoom.get(postId);
    }

    public WebSocketSession findSessionByMemberId(Long memberId) {
        return sessions.get(memberId);
    }

    public WebSocketSession findAllChatRoomSessionByMemberId(Long memberId) {
        return chatRoomListSessions.get(memberId);
    }
    public void saveMemberInPost(Long postId, Long memberId) {
        Set<Long> members = new HashSet<>();
        if (postRoom.containsKey(postId)) {
            members = postRoom.get(postId);
        }
        members.add(memberId);
        postRoom.put(postId, members);
        log.info("{}번 게시글에 {}번 유저 입장", postId, memberId);
    }

    public void saveMemberInChat(Long roomId, Long memberId) {
        Set<Long> members = new HashSet<>();
        if(chatRoom.containsKey(roomId)){
            members = chatRoom.get(roomId);
        }
        members.add(memberId);
        chatRoom.put(roomId, members);
        log.info("{}번 채팅방에 {}번 유저 입장",  roomId, memberId);
    }

    public void saveSessionWithMemberId(Long memberId, WebSocketSession webSocketSession) {
        sessions.put(memberId, webSocketSession);
        log.info("{}번 유저 웹소켓 세션 저장 성공", memberId);
    }

    public void saveChatRoomListSessionWithMemberId(Long memberId, WebSocketSession session) {
        chatRoomListSessions.put(memberId, session);
        log.info("{}번 유저 웹소켓 세션 저장 성공 - 목록", memberId);
    }

    public void deleteMemberInPost(Long postId, Long memberId) {
        Set<Long> members = postRoom.get(postId);
        if(members == null)
            return;
        members.remove(memberId);
        if(members.isEmpty())
            postRoom.remove(postId);
    }

    public void deleteMemberInChatRoom(Long roomId, Long memberId) {
        Set<Long> members = chatRoom.get(roomId);
        if (members != null) {
            members.remove(memberId);
            if (members.isEmpty()) {
                chatRoom.remove(roomId);
            }
        }
    }

    public void deleteSessionWithMemberId(Long memberId) {
        sessions.remove(memberId);
        log.info("세션 삭제 완료! 세션 저장소 크키: {}", sessions.size());
    }
    public void deleteChatRoomListSessionWithMemberId(Long memberId) {
        chatRoomListSessions.remove(memberId);
        log.info("목록: 세션 삭제 완료! 세션 저장소 크키: {}", sessions.size());
    }

    // 게시글 만료시 호출
    public void deletePostRoom(Long postId) {
        postRoom.remove(postId);
    }

}
