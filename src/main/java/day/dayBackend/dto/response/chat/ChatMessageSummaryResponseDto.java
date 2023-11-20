package day.dayBackend.dto.response.chat;

import day.dayBackend.domain.chat.ChatMessage;
import lombok.Getter;

@Getter
public class ChatMessageSummaryResponseDto {

    private String senderName;
    private String receiverName;
    private String content;

    public static ChatMessageSummaryResponseDto fromEntity(ChatMessage chatMessage) {
        ChatMessageSummaryResponseDto dto = new ChatMessageSummaryResponseDto();
        dto.senderName = chatMessage.getSender().getUsername();
        dto.receiverName = chatMessage.getReceiver().getUsername();
        dto.content = chatMessage.getContent();
        return dto;
    }
}
