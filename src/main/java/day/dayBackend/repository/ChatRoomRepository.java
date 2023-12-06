package day.dayBackend.repository;

import day.dayBackend.domain.chat.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    /**
     * 채팅방 저장
     */
    @Query(value = "select cr from ChatRoom cr " +
            "where cr.sender.id = :senderId and " +
            "cr.receiver.id = :receiverId or " +
            "cr.sender.id = :receiverId and " +
            "cr.receiver.id = :senderId ")
    Optional<ChatRoom> findBySenderAndReceiver(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    /**
     * 채팅방 조회
     */
    @Query(value = "select cr from ChatRoom cr where " +
            "cr.sender.id = :memberId and cr.senderDeletedAt is null or " +
            "cr.receiver.id = :memberId and cr.receiverDeletedAt is null ")
    Page<ChatRoom> findByReceiverId(@Param("memberId") Long memberId, Pageable pageable);

    /**
     * 삭제하고 싶은 채팅방 단건 조회
     */
    @Query(value = "select cr from ChatRoom cr " +
            "where (cr.sender.id = :memberId or cr.receiver.id = :memberId) " +
            "and cr.id = :chatRoomId ")
    Optional<ChatRoom> findByMemberAndChatRoom(@Param("memberId") Long memberId, @Param("chatRoomId") String chatRoomId);
}
