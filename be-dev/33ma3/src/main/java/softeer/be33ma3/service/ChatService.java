package softeer.be33ma3.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softeer.be33ma3.domain.*;
import softeer.be33ma3.dto.request.ChatMessageDto;
import softeer.be33ma3.dto.response.ChatHistoryDto;
import softeer.be33ma3.dto.response.AllChatRoomDto;
import softeer.be33ma3.dto.response.ChatMessageResponseDto;
import softeer.be33ma3.dto.response.LastMessageDto;
import softeer.be33ma3.exception.BusinessException;
import softeer.be33ma3.repository.*;
import softeer.be33ma3.repository.ChatMessageRepository;
import softeer.be33ma3.repository.ChatRoomRepository;
import softeer.be33ma3.repository.PostRepository;
import softeer.be33ma3.websocket.WebSocketRepository;
import softeer.be33ma3.websocket.WebSocketService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static softeer.be33ma3.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChatService {
    private final ObjectMapper objectMapper;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final WebSocketRepository webSocketRepository;
    private final WebSocketService webSocketService;

    @Transactional
    public Long createRoom(Member client, Long centerId, Long postId) {
        Member center = memberRepository.findById(centerId).orElseThrow(() -> new BusinessException(NOT_FOUND_CENTER));
        Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessException(NOT_FOUND_POST));

        if(!post.getMember().getMemberId().equals(client.getMemberId())){   //게시글 작성자만 생성할 수 있음
            throw new BusinessException(ONLY_POST_AUTHOR_ALLOWED);
        }
        if(chatRoomRepository.findRoomIdByCenterIdAndClientId(centerId, client.getMemberId()).isPresent()){     // 이미 방이 존재하는 경우
            return chatRoomRepository.findRoomIdByCenterIdAndClientId(centerId, client.getMemberId()).get();    // 기존 방 아이디 반환
        }

        ChatRoom chatRoom = ChatRoom.createChatRoom(client, center);
        return chatRoomRepository.save(chatRoom).getChatRoomId();
    }

    public List<AllChatRoomDto> showAllChatRoom(Member member) {
        List<AllChatRoomDto> allChatRoomDto = new ArrayList<>();

        if(member.isClient()){
            List<ChatRoom> chatRooms = chatRoomRepository.findByClient_MemberId(member.getMemberId());
            for (ChatRoom chatRoom : chatRooms) {
                allChatRoomDto.add(getChatDto(chatRoom, chatRoom.getCenter().getLoginId(), member));
            }
        }
        if(member.isCenter()) {
            List<ChatRoom> chatRooms = chatRoomRepository.findByCenter_MemberId(member.getMemberId());
            for (ChatRoom chatRoom : chatRooms) {
                allChatRoomDto.add(getChatDto(chatRoom, chatRoom.getClient().getLoginId(), member));
            }
        }

        return allChatRoomDto;
    }

    @Transactional
    public void sendChatMessage(ChatMessageDto chatMessageDto) {
        Member sender = memberRepository.findById(chatMessageDto.getSenderId()).get();
        Member receiver = memberRepository.findById(chatMessageDto.getReceiverId()).get();
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getRoomId()).get();

        //메세지 저장
        ChatMessage chatMessage = ChatMessage.createChatMessage(sender, chatRoom, chatMessageDto.getMessage());
        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

        directUpdateAllChatRoom(chatRoom, receiver.getLoginId(), sender); //나의 목록 실시간 업데이트

        if(!webSocketRepository.isMemberInChatRoom(chatRoom.getChatRoomId(), receiver.getMemberId())){      //채팅룸에 상대방이 존재하지 않을 경우
            if(webSocketRepository.findAllChatRoomSessionByMemberId(receiver.getMemberId()) != null){   //목록 세션에 있는 경우
                directUpdateAllChatRoom(chatRoom, sender.getLoginId(), receiver);   //상대방 목록 실시간 업데이트
                return;
            }
            return;
        }

        sendDirectToReceiver(savedChatMessage, chatRoom, sender, receiver);     //채팅방에 상대방이 존재하는 경우
    }

    private void directUpdateAllChatRoom(ChatRoom chatRoom, String memberName, Member sender) {
        AllChatRoomDto chatDto = getChatDto(chatRoom, memberName, sender); //전송할 목록
        webSocketService.sendAllChatData2Client(sender.getMemberId(), chatDto);  //목록 실시간 전송
    }

    private void sendDirectToReceiver(ChatMessage savedChatMessage, ChatRoom chatRoom, Member sender, Member receiver) {
        savedChatMessage.setReadDoneTrue();   //읽음 처리

        //receiver 에게 전송
        ChatMessageResponseDto chatMessageResponseDto = ChatMessageResponseDto.create(savedChatMessage);
        directUpdateAllChatRoom(chatRoom, sender.getLoginId(), receiver);   //상대방 목록 실시간 업데이트
        webSocketService.sendData2Client(receiver.getMemberId(), chatMessageResponseDto);   //채팅 내용 실시간 전송
    }

    private AllChatRoomDto getChatDto(ChatRoom chatRoom, String memberName, Member member) {
        LastMessageDto lastMessage = chatMessageRepository.findLastMessageByChatRoomId(chatRoom.getChatRoomId());//마지막 메세지

        if(lastMessage == null){    //방이 만들어지고 메세지를 보내지 않은 경우
            return AllChatRoomDto.create(chatRoom, "", memberName, 0, LocalDateTime.now());
        }

        int count = (int) chatMessageRepository.countReadDoneIsFalse(chatRoom.getChatRoomId(), member.getMemberId());     //안읽은 메세지 개수
        return AllChatRoomDto.create(chatRoom, lastMessage.getMessage(), memberName, count, lastMessage.getCreateTime());
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
            chatHistoryDtos.add(ChatHistoryDto.getChatHistoryDto(chatMessage, chatMessage.getCreateTime()));
        }

        return chatHistoryDtos;
    }

    private void isValidRoomMember(Member member, ChatRoom chatRoom) {
        if (member.isClient() && !chatRoom.getClient().getMemberId().equals(member.getMemberId())) {
            throw new BusinessException(NOT_A_MEMBER_OF_ROOM);
        }

        if (member.isCenter() && !chatRoom.getCenter().getMemberId().equals(member.getMemberId())) {
            throw new BusinessException(NOT_A_MEMBER_OF_ROOM);
        }
    }
}
