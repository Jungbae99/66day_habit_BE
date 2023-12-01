package day.dayBackend.domain.chat;

import day.dayBackend.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Member sender;

    @OneToOne(fetch = FetchType.LAZY)
    private Member receiver;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<ChatMessage> chatMessageList;

    private LocalDateTime senderDeletedAt;

    private LocalDateTime receiverDeletedAt;

    private LocalDateTime senderChatDeletedAt;

    private LocalDateTime receiverChatDeletedAt;

    @Builder
    ChatRoom(Long id, Member sender, Member receiver) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
    }

    public void deleteSender() {
        this.senderDeletedAt = LocalDateTime.now();
        this.senderChatDeletedAt = LocalDateTime.now();
    }

    public void deleteReceiver() {
        this.receiverDeletedAt = LocalDateTime.now();
        this.receiverChatDeletedAt = LocalDateTime.now();
    }

    public void updateDeletedAt() {
        this.senderDeletedAt = null;
        this.receiverDeletedAt = null;
    }
}