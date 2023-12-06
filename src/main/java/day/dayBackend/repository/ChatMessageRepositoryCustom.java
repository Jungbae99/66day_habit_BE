package day.dayBackend.repository;

import day.dayBackend.domain.chat.ChatMessage;
import day.dayBackend.domain.chat.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepositoryCustom {

    Page<ChatMessage> findByChatRoomId(ChatRoom chatRoomId, Long memberId, Pageable pageable);
}
