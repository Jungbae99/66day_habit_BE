package day.dayBackend.dto.response.chat;

import day.dayBackend.domain.Member;
import day.dayBackend.domain.chat.ChatMessage;
import day.dayBackend.domain.chat.ChatRoom;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatRoomSummaryResponseDto {

    private Long chatRoomId;
    private Long senderId;
    private String senderName;
    private String senderProfileImage;
    private String lastMessage;

    public static ChatRoomSummaryResponseDto fromEntity(ChatRoom chatRoom, Member member) {

        ChatRoomSummaryResponseDto dto = new ChatRoomSummaryResponseDto();
        List<ChatMessage> chatMessageList = chatRoom.getChatMessageList();
        String content = chatMessageList.get(chatMessageList.size() - 1).getContent();

        dto.chatRoomId = chatRoom.getId();

        if (chatRoom.getSender().equals(member)) {
            dto.senderId = chatRoom.getReceiver().getId();
            dto.senderName = chatRoom.getReceiver().getUsername();
            dto.senderProfileImage = chatRoom.getReceiver().getProfileImage().getUrl();
            dto.lastMessage = content;
        } else {
            dto.senderId = chatRoom.getSender().getId();
            dto.senderName = chatRoom.getSender().getUsername();
            dto.senderProfileImage = chatRoom.getSender().getProfileImage().getUrl();
            dto.lastMessage = content;
        }

        return dto;
    }

}
