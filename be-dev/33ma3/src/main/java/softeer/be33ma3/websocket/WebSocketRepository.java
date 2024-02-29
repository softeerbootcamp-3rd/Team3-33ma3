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
    private static final Map<Long, Set<Long>> postRoom = new ConcurrentHashMap<>();    // postId : set of memberId
    private static final Map<Long, Set<Long>> chatRoom = new ConcurrentHashMap<>();    // roomId : set of memberId
    private static final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();     // memberId : WebSocketSession
    private static final Map<Long, WebSocketSession> allChatRoomSessions = new ConcurrentHashMap<>();      //memberId: WebSocketSession

    public Set<Long> findAllMemberInPost(Long postId) {
        return postRoom.get(postId);
    }
    public boolean isMemberInChatRoom(Long roomId, Long memberId){
        Set<Long> members = chatRoom.get(roomId);
        // 만약 members가 null이면 해당 roomId에 대한 채팅 룸이 존재하지 않음
        if (members == null) {
            return false;
        }
        // Set<Long>에 memberId가 포함되어 있는지 확인
        return members.contains(memberId);
    }

    public Long findReceiverInChatRoom(Long roomId, Long memberId) {
        Set<Long> memberIds = chatRoom.get(roomId);

        if(memberIds != null){
            return memberIds.stream()
                    .filter(id -> !id.equals(memberId))
                    .findFirst()
                    .orElse(null); // 해당하는 멤버가 없을 경우
        }

        return null;
    }

    public WebSocketSession findSessionByMemberId(Long memberId) {
        return sessions.get(memberId);
    }

    public WebSocketSession findAllChatRoomSessionByMemberId(Long memberId) {
        return allChatRoomSessions.get(memberId);
    }
    public void saveMemberInPost(Long postId, Long memberId) {
        Set<Long> members = (postRoom.containsKey(postId)) ? postRoom.get(postId) : new HashSet<>();
        members.add(memberId);
        postRoom.put(postId, members);
        log.info("{}번 게시글에 {}번 유저 입장", postId, memberId);
        log.info("{}번 게시글에 들어와 있는 유저: {}명", postId, postRoom.get(postId).size());
    }

    public void saveMemberInChat(Long roomId, Long memberId) {
        Set<Long> members = (chatRoom.containsKey(roomId)) ? chatRoom.get(roomId) : new HashSet<>();
        members.add(memberId);
        chatRoom.put(roomId, members);
        log.info("{}번 채팅방에 {}번 유저 입장",  roomId, memberId);
    }

    public void saveSessionWithMemberId(Long memberId, WebSocketSession webSocketSession) {
        if(sessions.get(memberId) == null || !sessions.get(memberId).isOpen()) {
            sessions.put(memberId, webSocketSession);
            log.info("{}번 유저 웹소켓 세션 저장 성공", memberId);
            log.info("웹소켓 세션 저장소 크기: {}" , sessions.size());
        }
        else {
            log.info("{}번 유저는 이미 웹소켓 세션이 존재합니다.", memberId);
        }
    }

    public void saveAllChatRoomSessionWithMemberId(Long memberId, WebSocketSession session) {
        if(allChatRoomSessions.get(memberId) == null || !allChatRoomSessions.get(memberId).isOpen()) {
            allChatRoomSessions.put(memberId, session);
            log.info("{}번 유저 웹소켓 세션 저장 성공 - 목록", memberId);
        }
        else {
            log.info("{}번 유저는 이미 채팅 목록에 대한 웹소켓 세션이 존재합니다.", memberId);
        }
    }

    public void deleteMemberInPost(Long postId, Long memberId) {
        Set<Long> members = postRoom.get(postId);
        if(members == null) {
            return;
        }
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

    public void deleteAllChatRoomSessionWithMemberId(Long memberId) {
        allChatRoomSessions.remove(memberId);
        log.info("목록: 세션 삭제 완료! 세션 저장소 크키: {}", sessions.size());
    }

    // 게시글 만료시 호출
    public void deletePostRoom(Long postId) {
        postRoom.remove(postId);
    }
}
