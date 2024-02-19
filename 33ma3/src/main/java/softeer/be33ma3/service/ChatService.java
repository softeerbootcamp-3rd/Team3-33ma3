package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.*;
import softeer.be33ma3.dto.response.ChatHistoryDto;
import softeer.be33ma3.dto.response.ChatRoomListDto;
import softeer.be33ma3.dto.response.ChatMessageResponseDto;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.repository.*;
import softeer.be33ma3.websocket.WebSocketHandler;
import softeer.be33ma3.websocket.WebSocketRepository;

import java.util.ArrayList;
import java.util.List;

import static softeer.be33ma3.exception.ErrorCode.*;
import static softeer.be33ma3.service.MemberService.CENTER_TYPE;
import static softeer.be33ma3.service.MemberService.CLIENT_TYPE;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final WebSocketHandler webSocketHandler;
    private final WebSocketRepository webSocketRepository;
    private final AlertRepository alertRepository;
    private final CenterRepository centerRepository;

    @Transactional
    public Long createRoom(Member client, Long centerId, Long postId) {
        Member center = memberRepository.findById(centerId).orElseThrow(() -> new BusinessException(NOT_FOUND_CENTER));
        Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessException(NOT_FOUND_POST));

        if(!post.getMember().equals(client)){
            throw new BusinessException(ONLY_POST_AUTHOR_ALLOWED);
        }
        if(chatRoomRepository.findRoomIdByCenterIdAndClientId(centerId, client.getMemberId()).isPresent()){     // 이미 방이 존재하는 경우
            return chatRoomRepository.findRoomIdByCenterIdAndClientId(centerId, client.getMemberId()).get();    // 기존 방 아이디 반환
        }

        ChatRoom chatRoom = ChatRoom.createChatRoom(client, center);
        return chatRoomRepository.save(chatRoom).getChatRoomId();
    }

    @Transactional
    public void sendMessage(Member sender, Long roomId, Long receiverId, String contents) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new BusinessException(NOT_FOUND_CHAT_ROOM));
        Member receiver = memberRepository.findById(receiverId).orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER));

        if(sender.getMemberType() == CLIENT_TYPE){
            if(!(chatRoom.getClient().equals(sender) && chatRoom.getCenter().equals(receiver))){
                throw new BusinessException(NOT_A_MEMBER_OF_ROOM);
            }
        }

        ChatMessage chatMessage = ChatMessage.createChatMessage(sender, chatRoom, contents);
        chatMessage = chatMessageRepository.save(chatMessage);

        if(webSocketRepository.findSessionByMemberId(receiver.getMemberId()) == null){  //채팅룸에 상대방이 존재하지 않을 경우
            Alert alert = Alert.createAlert(chatRoom.getChatRoomId(), receiver);  //알림 테이블에 저장
            alertRepository.save(alert);
            return;
        }
        //채팅룸에 상대방이 존재하는 경우
        chatMessage.setReadDoneTrue();   //읽음 처리
        ChatMessageResponseDto chatMessageResponseDto = ChatMessageResponseDto.create(chatMessage);
        webSocketHandler.sendData2Client(receiver.getMemberId(), chatMessageResponseDto);   //실시간 전송
    }

    public List<ChatRoomListDto> showAllChatRoom(Member member) {
        List<ChatRoomListDto> allChatRoomListDto = new ArrayList<>();

        if(member.getMemberType() == CLIENT_TYPE){
            List<ChatRoom> chatRooms = chatRoomRepository.findByClient_MemberId(member.getMemberId());
            for (ChatRoom chatRoom : chatRooms) {
                Center center = centerRepository.findByMember_MemberId(chatRoom.getCenter().getMemberId()).get();
                allChatRoomListDto.add(getChatDto(chatRoom, center.getCenterName(), member));
            }
        }

        if(member.getMemberType() == CENTER_TYPE) {
            List<ChatRoom> chatRooms = chatRoomRepository.findByCenter_MemberId(member.getMemberId());
            for (ChatRoom chatRoom : chatRooms) {
                allChatRoomListDto.add(getChatDto(chatRoom, chatRoom.getClient().getLoginId(), member));
            }
        }

        return allChatRoomListDto;
    }

    @Transactional
    public List<ChatHistoryDto> showOneChatHistory(Member member, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅 룸"));
        validateMember(member, chatRoom);

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoom_ChatRoomId(chatRoom.getChatRoomId());

        List<ChatHistoryDto> chatHistoryDtos = new ArrayList<>();
        for (ChatMessage chatMessage : chatMessages) {
            if(!chatMessage.getSender().equals(member) && !chatMessage.isReadDone()){   //상대방이 보낸 메세지의 읽음 여부가 false인 경우
                chatMessage.setReadDoneTrue();      //읽음 처리
            }
            chatHistoryDtos.add(ChatHistoryDto.getChatHistoryDto(chatMessage));
        }

        return chatHistoryDtos;
    }

    private void validateMember(Member member, ChatRoom chatRoom) {
        if (member.getMemberType() == CLIENT_TYPE && !chatRoom.getClient().equals(member)) {
            throw new BusinessException(NOT_A_MEMBER_OF_ROOM);
        }

        if (member.getMemberType() == CENTER_TYPE && !chatRoom.getCenter().equals(member)) {
            throw new BusinessException(NOT_A_MEMBER_OF_ROOM);
        }
    }

    private ChatRoomListDto getChatDto(ChatRoom chatRoom, String memberName, Member member) {
        String lastChatMessage = chatMessageRepository.findLastMessageByChatRoomId(chatRoom.getChatRoomId());      //마지막 메세지
        int count = (int) chatMessageRepository.countReadDoneIsFalse(chatRoom.getChatRoomId(), member.getMemberId());     //안읽은 메세지 개수

        return ChatRoomListDto.createChatRoomDto(chatRoom, lastChatMessage, memberName, count);
    }
}
