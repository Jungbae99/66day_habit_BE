package day.dayBackend.dto.response.chat;

import day.dayBackend.domain.Member;
import day.dayBackend.domain.chat.ChatRoom;
import lombok.Getter;

@Getter
public class ChatRoomSummaryResponseDto {

    private Long chatRoomId;
    private Long senderId;
    private String senderName;
    private String senderProfileImage;

    public static ChatRoomSummaryResponseDto fromEntity(ChatRoom chatRoom, Member member) {

        ChatRoomSummaryResponseDto dto = new ChatRoomSummaryResponseDto();
        dto.chatRoomId = chatRoom.getId();

        if (chatRoom.getSender().equals(member)) {
            dto.senderId = chatRoom.getReceiver().getId();
            dto.senderName = chatRoom.getReceiver().getUsername();
            dto.senderProfileImage = chatRoom.getReceiver().getProfileImage().getUrl();
        } else {
            dto.senderId = chatRoom.getSender().getId();
            dto.senderName = chatRoom.getSender().getUsername();
            dto.senderProfileImage = chatRoom.getSender().getProfileImage().getUrl();
        }

        return dto;
    }

}
