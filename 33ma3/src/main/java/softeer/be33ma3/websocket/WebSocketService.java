package softeer.be33ma3.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import softeer.be33ma3.domain.ChatMessage;
import softeer.be33ma3.domain.ChatRoom;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.dto.request.ChatMessageDto;
import softeer.be33ma3.dto.response.AllChatRoomDto;
import softeer.be33ma3.dto.response.ChatMessageResponseDto;
import softeer.be33ma3.repository.ChatMessageRepository;
import softeer.be33ma3.repository.ChatRoomRepository;
import softeer.be33ma3.repository.MemberRepository;

import java.io.IOException;
import java.util.Set;

import static softeer.be33ma3.utils.StringParser.createTimeParsing;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final WebSocketRepository webSocketRepository;
    private final ObjectMapper objectMapper;

    public void receiveExitMsg(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        // 게시글 관련 웹 소켓 연결이 종료된 유저가 있다고 메세지를 받았을 경우
        if(payload.contains("post") && payload.contains("memberId")) {
            log.info("게시글에서 유저가 나갔습니다.");
            ExitRoomMember exitRoomMember = objectMapper.readValue(payload, ExitRoomMember.class);
            closePostConnection(exitRoomMember.getRoomId(), exitRoomMember.getMemberId());
        }
        if(payload.contains("chat") && payload.contains("memberId")) {
            log.info("채팅방에서 유저가 나갔습니다.");
            ExitRoomMember exitRoomMember = objectMapper.readValue(payload, ExitRoomMember.class);
            closeChatConnection(exitRoomMember.getRoomId(), exitRoomMember.getMemberId());
        }
        if(payload.contains("chatRoom") && payload.contains("memberId")){
            log.info("채팅 목록에서 유저가 나갔습니다.");
            ExitMember exitMember = objectMapper.readValue(payload, ExitMember.class);
            closeAllChatRoomConnection(exitMember.getMemberId());
        }

        log.info("메세지 수신 성공: {}", payload); // 수신한 메세지 log
        TextMessage textMessage = new TextMessage("메세지 수신 성공");
        session.sendMessage(textMessage);
    }

    @Transactional
    public void sendChatMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        //payload -> chatMessageDto 로 변환
        ChatMessageDto chatMessageDto = objectMapper.readValue(payload, ChatMessageDto.class);

        Member sender = memberRepository.findById(chatMessageDto.getSenderId()).get();
        Member receiver = memberRepository.findById(chatMessageDto.getReceiverId()).get();
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getRoomId()).get();

        ChatMessage chatMessage = ChatMessage.createChatMessage(sender, chatRoom, chatMessageDto.getMessage());
        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

        AllChatRoomDto chatDto = getChatDto(chatRoom, receiver.getLoginId(), sender); //업데이트 할 목록 생성
        sendAllChatData2Client(sender.getMemberId(), chatDto);  //목록 실시간 업데이트

        if(!webSocketRepository.isMemberInChatRoom(chatRoom.getChatRoomId(), receiver.getMemberId())){      //채팅룸에 상대방이 존재하지 않을 경우
            if(webSocketRepository.findAllChatRoomSessionByMemberId(receiver.getMemberId()) != null){   //목록 세션에 있는 경우
                chatDto = getChatDto(chatRoom, sender.getLoginId(), receiver);     //업데이트 할 목록 생성
                sendAllChatData2Client(receiver.getMemberId(), chatDto);   //실시간 전송 - 채팅 목록
                return;
            }
        }

        //채팅룸에 상대방이 존재하는 경우
        savedChatMessage.setReadDoneTrue();   //읽음 처리
        ChatMessageResponseDto chatMessageResponseDto = ChatMessageResponseDto.create(savedChatMessage, createTimeParsing(savedChatMessage.getCreateTime()));
        sendData2Client(receiver.getMemberId(), chatMessageResponseDto);   //실시간 전송 - 채팅 내용
    }

    public void sendAllChatData2Client(Long memberId, Object data) {
        try {
            // 클라이언트에 해당하는 세션 가져오기
            WebSocketSession session = webSocketRepository.findAllChatRoomSessionByMemberId(memberId);
            // data 직렬화
            String jsonString = objectMapper.writeValueAsString(data);
            // 데이터 전송
            session.sendMessage(new TextMessage(jsonString));
        } catch(IOException e) {
            log.error("실시간 데이터 전송 에러");
        }
    }

    public void saveInPost(Long postId, Long memberId, WebSocketSession session) {
        webSocketRepository.saveMemberInPost(postId, memberId);
        webSocketRepository.saveSessionWithMemberId(memberId, session);
    }

    public void saveInChat(Long roomId, Long memberId, WebSocketSession session){
        webSocketRepository.saveMemberInChat(roomId, memberId);
        webSocketRepository.saveSessionWithMemberId(memberId, session);

    }

    public void saveInAllChatRoom(Long memberId, WebSocketSession session) {
        webSocketRepository.saveAllChatRoomSessionWithMemberId(memberId, session);
    }

    private void closePostConnection(Long postId, Long memberId) {
        webSocketRepository.deleteMemberInPost(postId, memberId);
        webSocketRepository.deleteSessionWithMemberId(memberId);
    }

    public void closeChatConnection(Long roomId, Long memberId) {
        webSocketRepository.deleteMemberInChatRoom(roomId, memberId);
        webSocketRepository.deleteSessionWithMemberId(memberId);
    }

    private void closeAllChatRoomConnection(Long memberId) {
        webSocketRepository.deleteAllChatRoomSessionWithMemberId(memberId);
    }

    // 서버 -> 클라이언트 데이터 전송
    public void sendData2Client(Long memberId, Object data){
        try{
            if(memberId == null || data == null){
                return;
            }
            // 클라이언트에 해당하는 세션 가져오기
            WebSocketSession session = webSocketRepository.findSessionByMemberId(memberId);
            if(session == null || !session.isOpen()) {
                log.info("웹 소켓 연결이 되어있지 않음");
                return;
            }
            // data 직렬화
            String jsonString = objectMapper.writeValueAsString(data);
            // 데이터 전송
            session.sendMessage(new TextMessage(jsonString));
        }catch (IOException e){
            log.error("실시간 데이터 전송 에러");
        }
    }

    public void deletePostRoom(Long postId) {
        webSocketRepository.deletePostRoom(postId);
    }
    public Set<Long> findAllMemberInPost(Long postId) {
        return webSocketRepository.findAllMemberInPost(postId);
    }
    public boolean isInPostRoom(Long postId, Long memberId) {
        Set<Long> memberIds = webSocketRepository.findAllMemberInPost(postId);
        if(memberIds == null){
            return false;
        }
        return memberIds.contains(memberId);
    }
    private AllChatRoomDto getChatDto(ChatRoom chatRoom, String memberName, Member member) {
        ChatMessage lastChatMessage = chatMessageRepository.findLastMessageByChatRoomId(chatRoom.getChatRoomId());//마지막 메세지
        if(lastChatMessage == null){    //방이 만들어지고 메세지를 보내지 않은 경우
            return AllChatRoomDto.create(chatRoom, "", memberName, 0, "");
        }
        int count = (int) chatMessageRepository.countReadDoneIsFalse(chatRoom.getChatRoomId(), member.getMemberId());     //안읽은 메세지 개수
        return AllChatRoomDto.create(chatRoom, lastChatMessage.getContents(), memberName, count, createTimeParsing(lastChatMessage.getCreateTime()));
    }
}
