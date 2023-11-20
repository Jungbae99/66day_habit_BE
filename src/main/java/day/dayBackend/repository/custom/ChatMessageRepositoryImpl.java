package day.dayBackend.repository.custom;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;

import day.dayBackend.domain.chat.ChatMessage;
import day.dayBackend.domain.chat.ChatRoom;
import day.dayBackend.domain.chat.QChatMessage;
import day.dayBackend.repository.ChatMessageRepositoryCustom;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {

    private final EntityManager em;

    @Override
    public Page<ChatMessage> findByChatRoomId(ChatRoom chatRoom, Long memberId, Pageable pageable) {

        JPQLQueryFactory queryFactory = new JPAQueryFactory(em);
        QChatMessage chatMessage = QChatMessage.chatMessage;

        JPQLQuery<ChatMessage> query = queryFactory.selectFrom(chatMessage)
                .where(compareMemberId(memberId, chatRoom, chatMessage))
                .orderBy(chatMessage.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(query.fetch(), pageable, query::fetchCount);
    }

    private BooleanExpression compareMemberId(Long memberId, ChatRoom chatRoom, QChatMessage chatMessage) {
        if (chatRoom.getSender().getId().equals(memberId)) {
            if (chatRoom.getSenderChatDeletedAt() != null) {
                return chatMessage.createdAt.after(chatRoom.getSenderChatDeletedAt());
            }
        } else {
            if (chatRoom.getReceiverChatDeletedAt() != null) {
                return chatMessage.createdAt.after(chatRoom.getReceiverChatDeletedAt());
            }
        }
        return null;
    }
}
