package softeer.be33ma3.repository.Chat;

import softeer.be33ma3.dto.response.LastMessageDto;

public interface ChatMessageCustomRepository {
    LastMessageDto findLastMessageByChatRoomId(Long chatRoomId);
}
