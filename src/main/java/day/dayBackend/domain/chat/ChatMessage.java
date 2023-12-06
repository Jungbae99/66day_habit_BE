package day.dayBackend.domain.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import day.dayBackend.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @OneToOne(fetch = FetchType.LAZY)
    private Member sender;

    @OneToOne(fetch = FetchType.LAZY)
    private Member receiver;

    private String content;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Builder
    ChatMessage(ChatRoom chatRoom, Member sender, Member receiver, String content) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }
}