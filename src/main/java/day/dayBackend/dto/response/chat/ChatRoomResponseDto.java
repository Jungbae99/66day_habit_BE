package day.dayBackend.dto.response.chat;

import lombok.Getter;

import java.util.List;

@Getter
public class ChatRoomResponseDto {

    private Long totalRows; // 채팅방 수 
    private Long totalPages; // 페이지 수
    private List<ChatRoomSummaryResponseDto> chatRoomList;

    public static ChatRoomResponseDto of(long totalRows, long totalPages, List<ChatRoomSummaryResponseDto> chatRoomList) {
        ChatRoomResponseDto dto = new ChatRoomResponseDto();
        dto.totalRows = totalRows;
        dto.totalPages = totalPages;
        dto.chatRoomList = chatRoomList;
        return dto;
    }
}
