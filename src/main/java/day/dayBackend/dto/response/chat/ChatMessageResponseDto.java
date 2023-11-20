package day.dayBackend.dto.response.chat;

import lombok.Getter;

import java.util.List;

@Getter
public class ChatMessageResponseDto {

    private Long totalRows; // 메시지 수
    private Long totalPages; // 페이지 수
    private List<ChatMessageSummaryResponseDto> chatMessageList;

    public static ChatMessageResponseDto of(long totalRows, long totalPages, List<ChatMessageSummaryResponseDto> chatMessageList) {
        ChatMessageResponseDto dto = new ChatMessageResponseDto();
        dto.totalRows = totalRows;
        dto.totalPages = totalPages;
        dto.chatMessageList = chatMessageList;
        return dto;
    }
}
