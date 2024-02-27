package softeer.be33ma3.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import softeer.be33ma3.dto.response.LastMessageDto;

import static softeer.be33ma3.domain.QChatMessage.chatMessage;

@Repository
@RequiredArgsConstructor
public class ChatMessageCustomRepositoryImpl implements ChatMessageCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public LastMessageDto findLastMessageByChatRoomId(Long chatRoomId) {
        LastMessageDto lastMessage = jpaQueryFactory
                .select(Projections.constructor(LastMessageDto.class,
                        chatMessage.contents,
                        chatMessage.createTime))
                .from(chatMessage)
                .where(chatMessage.chatRoom.chatRoomId.eq(chatRoomId))
                .orderBy(chatMessage.createTime.desc())
                .fetchFirst();

        return lastMessage;
    }
}
