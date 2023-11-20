package day.dayBackend.service;

import day.dayBackend.dto.response.chat.ChatRoomResponseDto;
import day.dayBackend.dto.response.chat.ChatRoomSummaryResponseDto;
import day.dayBackend.dto.request.chat.MessageRequestDto;
import day.dayBackend.domain.Member;
import day.dayBackend.domain.chat.ChatMessage;
import day.dayBackend.domain.chat.ChatRoom;
import day.dayBackend.exception.NotFoundException;
import day.dayBackend.repository.ChatMessageRepository;
import day.dayBackend.repository.ChatRoomRepository;
import day.dayBackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;

    /**
     * 채팅방을 저장한다.
     */
    @Transactional
    public void chatRoomSave(MessageRequestDto dto) {

        Member sender = memberRepository.findByIdAndDeletedAtNull(dto.getSenderId())
                .orElseThrow(() -> new NotFoundException("id에 해당하는 발신자가 존재하지 않습니다."));

        Member receiver = memberRepository.findByIdAndDeletedAtNull(dto.getReceiverId())
                .orElseThrow(() -> new NotFoundException("id에 해당하는 수신자가 존재하지 않습니다."));

        if (!chatRoomRepository.findBySenderAndReceiver(sender.getId(), receiver.getId()).isPresent()) {
            ChatRoom chatRoom = ChatRoom.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .build();

            chatMessageSave(sender, receiver, chatRoom, dto);
            chatRoomRepository.save(chatRoom);
        } else {
            ChatRoom chatRoom = chatRoomRepository.findBySenderAndReceiver(dto.getSenderId(), dto.getReceiverId())
                    .orElseThrow(() -> new NotFoundException("id에 해당하는 채팅방이 존재하지 않습니다."));
            chatRoom.updateDeletedAt();
            chatMessageSave(sender, receiver, chatRoom, dto);
        }
    }

    /**
     * 채팅 메시지를 저장한다.
     */
    private void chatMessageSave(Member sender, Member receiver, ChatRoom chatRoom, MessageRequestDto dto) {
        System.out.println("dto.getData() = " + dto.getContent());
        ChatMessage chatMessage = ChatMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .chatRoom(chatRoom)
                .content(dto.getContent())
                .build();

        chatMessageRepository.save(chatMessage);
    }

    /**
     * 채팅방 모두 가져오기
     */
    public ChatRoomResponseDto findAllChatRoom(Long memberId, Pageable pageable) {
        Page<ChatRoom> chatRoomList = chatRoomRepository.findByReceiverId(memberId, pageable);
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원이 존재하지 않습니다."));
        return ChatRoomResponseDto.of(
                chatRoomList.getTotalElements(),
                chatRoomList.getTotalPages(),
                chatRoomList.getContent().stream().
                        map((ChatRoom chatRoom) -> ChatRoomSummaryResponseDto.fromEntity(chatRoom, member)).collect(Collectors.toList())
        );
    }

    /**
     * 채팅방 삭제
     *
     * <해당하는 Member에 대한 deletedAt 컬럼을 now 로 업데이트합니다.
     * 이 채팅방에 대한 메시지를 조회할 때에는 deletedAt 이후의 메시지만 가져옵니다.
     * 반면, 상대방의 deletedAt 이 없다면 상대방은 모든 메시지를 가져옵니다.>
     */
    @Transactional
    public Long deleteChatRoom(Long memberId, String chatRoomId) {

        ChatRoom chatRoom = chatRoomRepository.findByMemberAndChatRoom(memberId, chatRoomId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 채팅방이 존재하지 않습니다."));

        if (chatRoom.getSender().getId().equals(memberId)) {
            chatRoom.deleteSender();
        } else {
            chatRoom.deleteReceiver();
        }
        return chatRoom.getId();
    }

}
