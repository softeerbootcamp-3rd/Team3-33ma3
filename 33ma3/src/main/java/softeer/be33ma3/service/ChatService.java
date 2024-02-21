package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.*;
import softeer.be33ma3.dto.response.ChatHistoryDto;
import softeer.be33ma3.dto.response.AllChatRoomDto;
import softeer.be33ma3.dto.response.ChatMessageResponseDto;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.repository.*;
import softeer.be33ma3.repository.post.PostRepository;
import softeer.be33ma3.websocket.WebSocketHandler;
import softeer.be33ma3.websocket.WebSocketRepository;

import java.util.ArrayList;
import java.util.List;

import static softeer.be33ma3.exception.ErrorCode.*;
import static softeer.be33ma3.service.MemberService.CENTER_TYPE;
import static softeer.be33ma3.service.MemberService.CLIENT_TYPE;
import static softeer.be33ma3.utils.StringParser.createTimeParsing;

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

    @Transactional
    public Long createRoom(Member client, Long centerId, Long postId) {
        Member center = memberRepository.findById(centerId).orElseThrow(() -> new BusinessException(NOT_FOUND_CENTER));
        Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessException(NOT_FOUND_POST));

        if(!post.getMember().equals(client)){   //게시글 작성자만 생성할 수 있음
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
        isValidSenderAndReceiver(sender, chatRoom, receiver);      //보내는 사람, 받는 사람이 해당 방의 멤버가 맞는지 검증

        ChatMessage chatMessage = ChatMessage.createChatMessage(sender, chatRoom, contents);
        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

        AllChatRoomDto chatDto = getChatDto(chatRoom, sender.getLoginId(), receiver);     //업데이트 할 목록 생성
        if(webSocketRepository.findSessionByMemberId(receiver.getMemberId()) == null){      //채팅룸에 상대방이 존재하지 않을 경우
            if(webSocketRepository.findAllChatRoomSessionByMemberId(receiver.getMemberId()) == null){       //상대방이 채팅 목록 세션을 연결 안하고 있는 경우
                Alert alert = Alert.createAlert(chatRoom.getChatRoomId(), receiver);        //채팅방 & 채팅 목록에도 없는 경우는 알림 테이블에 저장
                alertRepository.save(alert);
                return;
            }
            webSocketHandler.sendAllChatData2Client(receiver.getMemberId(), chatDto);   //실시간 전송 - 채팅 목록
            return;
        }
        //채팅룸에 상대방이 존재하는 경우
        savedChatMessage.setReadDoneTrue();   //읽음 처리
        ChatMessageResponseDto chatMessageResponseDto = ChatMessageResponseDto.create(savedChatMessage, createTimeParsing(savedChatMessage.getCreateTime()));
        webSocketHandler.sendData2Client(receiver.getMemberId(), chatMessageResponseDto);   //실시간 전송 - 채팅 내용
        webSocketHandler.sendAllChatData2Client(sender.getMemberId(), chatDto);     //보낸사람 목록도 실시간 업데이트
    }

    public List<AllChatRoomDto> showAllChatRoom(Member member) {
        List<AllChatRoomDto> allChatRoomDto = new ArrayList<>();

        if(member.getMemberType() == CLIENT_TYPE){
            List<ChatRoom> chatRooms = chatRoomRepository.findByClient_MemberId(member.getMemberId());
            for (ChatRoom chatRoom : chatRooms) {
                allChatRoomDto.add(getChatDto(chatRoom, chatRoom.getCenter().getLoginId(), member));
            }
        }
        if(member.getMemberType() == CENTER_TYPE) {
            List<ChatRoom> chatRooms = chatRoomRepository.findByCenter_MemberId(member.getMemberId());
            for (ChatRoom chatRoom : chatRooms) {
                allChatRoomDto.add(getChatDto(chatRoom, chatRoom.getClient().getLoginId(), member));
            }
        }

        return allChatRoomDto;
    }

    @Transactional
    public List<ChatHistoryDto> showOneChatHistory(Member member, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new BusinessException(NOT_FOUND_CHAT_ROOM));
        isValidRoomMember(member, chatRoom);

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoom_ChatRoomId(chatRoom.getChatRoomId());

        List<ChatHistoryDto> chatHistoryDtos = new ArrayList<>();
        for (ChatMessage chatMessage : chatMessages) {
            if(!chatMessage.getSender().getMemberId().equals(member.getMemberId()) && !chatMessage.isReadDone()){   //상대방이 보낸 메세지의 읽음 여부가 false인 경우
                chatMessage.setReadDoneTrue();      //읽음 처리
            }
            chatHistoryDtos.add(ChatHistoryDto.getChatHistoryDto(chatMessage, createTimeParsing(chatMessage.getCreateTime())));
        }

        return chatHistoryDtos;
    }

    private AllChatRoomDto getChatDto(ChatRoom chatRoom, String memberName, Member member) {
        ChatMessage lastChatMessage = chatMessageRepository.findLastMessageByChatRoomId(chatRoom.getChatRoomId());//마지막 메세지
        if(lastChatMessage == null){    //방이 만들어지고 메세지를 보내지 않은 경우
            return AllChatRoomDto.create(chatRoom, "", memberName, 0, "");
        }
        int count = (int) chatMessageRepository.countReadDoneIsFalse(chatRoom.getChatRoomId(), member.getMemberId());     //안읽은 메세지 개수
        return AllChatRoomDto.create(chatRoom, lastChatMessage.getContents(), memberName, count, createTimeParsing(lastChatMessage.getCreateTime()));
    }

    private void isValidRoomMember(Member member, ChatRoom chatRoom) {
        if (member.getMemberType() == CLIENT_TYPE && !chatRoom.getClient().equals(member)) {
            throw new BusinessException(NOT_A_MEMBER_OF_ROOM);
        }

        if (member.getMemberType() == CENTER_TYPE && !chatRoom.getCenter().equals(member)) {
            throw new BusinessException(NOT_A_MEMBER_OF_ROOM);
        }
    }

    private void isValidSenderAndReceiver(Member sender, ChatRoom chatRoom, Member receiver) {
        Member client = chatRoom.getClient();
        Member center = chatRoom.getCenter();

        if (sender.getMemberType() == CLIENT_TYPE) {
            if (!(client.getMemberId().equals(sender.getMemberId()) && center.getMemberId().equals(receiver.getMemberId()))) {
                throw new BusinessException(NOT_A_MEMBER_OF_ROOM);
            }
        }
        if (sender.getMemberType() == CENTER_TYPE) {
            if (!(client.getMemberId().equals(receiver.getMemberId()) && center.getMemberId().equals(sender.getMemberId()))) {
                throw new BusinessException(NOT_A_MEMBER_OF_ROOM);
            }
        }
    }
}
