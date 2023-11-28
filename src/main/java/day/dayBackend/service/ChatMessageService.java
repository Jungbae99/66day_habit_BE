package day.dayBackend.service;

import day.dayBackend.dto.response.chat.ChatMessageResponseDto;
import day.dayBackend.dto.response.chat.ChatMessageSummaryResponseDto;
import day.dayBackend.domain.chat.ChatMessage;
import day.dayBackend.domain.chat.ChatRoom;
import day.dayBackend.exception.NotFoundException;
import day.dayBackend.repository.ChatMessageRepository;
import day.dayBackend.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * 채팅메시지 조회 
     */
    public ChatMessageResponseDto findAllMessage(Long chatRoomId, Long memberId, Pageable pageable) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 채팅방이 존재하지 않습니다."));

        Page<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoomId(chatRoom, memberId, pageable);
        return ChatMessageResponseDto.of(
                chatMessageList.getTotalElements(),
                chatMessageList.getTotalPages(),
                chatMessageList.getContent().stream()
                        .map(ChatMessageSummaryResponseDto::fromEntity).collect(Collectors.toList())
        );
    }

}
