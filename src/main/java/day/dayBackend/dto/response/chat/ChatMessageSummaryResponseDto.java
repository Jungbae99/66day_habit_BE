package day.dayBackend.dto.response.chat;

import day.dayBackend.domain.chat.ChatMessage;
import lombok.Getter;

@Getter
public class ChatMessageSummaryResponseDto {

    private String senderName;
    private Long senderId;
    private String senderImg;
    private String receiverName;
    private Long receiverId;
    private String receiverImg;
    private String content;

    public static ChatMessageSummaryResponseDto fromEntity(ChatMessage chatMessage) {
        ChatMessageSummaryResponseDto dto = new ChatMessageSummaryResponseDto();
        dto.senderName = chatMessage.getSender().getUsername();
        dto.senderId = chatMessage.getSender().getId();
        dto.senderImg = chatMessage.getSender().getProfileImage().getUrl();
        dto.receiverId = chatMessage.getReceiver().getId();
        dto.receiverName = chatMessage.getReceiver().getUsername();
        dto.receiverImg = chatMessage.getReceiver().getProfileImage().getUrl();
        dto.content = chatMessage.getContent();
        return dto;
    }
}
