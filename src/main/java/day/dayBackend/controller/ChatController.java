package day.dayBackend.controller;

import day.dayBackend.dto.response.chat.ChatMessageResponseDto;
import day.dayBackend.service.ChatMessageService;
import day.dayBackend.dto.response.chat.ChatRoomResponseDto;
import day.dayBackend.service.ChatRoomService;
import day.dayBackend.config.SecurityUtil;
import day.dayBackend.dto.response.CommonResponseDto;
import day.dayBackend.exception.NotAuthenticatedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/chat")
public class ChatController {

    private final ChatRoomService chatService;
    private final ChatMessageService chatMessageService;

    /**
     * 채팅방 모두 조회
     */
    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto getChatRoomV1(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                           @RequestParam(value = "limit", required = false, defaultValue = "100") int size
    ) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        Pageable pageable = PageRequest.of(page, size);
        return CommonResponseDto.<ChatRoomResponseDto>builder()
                .data(chatService.findAllChatRoom(memberId, pageable))
                .build();
    }

    /**
     * 채팅방 삭제
     */
    @DeleteMapping("/{chatRoomId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto deleteChatRoom(@PathVariable(name = "chatRoomId") String chatRoomId) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));

        return CommonResponseDto.<Map<String, Long>>builder()
                .data(Map.of("chatRoomId", chatService.deleteChatRoom(memberId, chatRoomId)))
                .build();
    }


    /**
     * 채팅 메시지 조회
     */
    @GetMapping("/get/{chatRoomId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public CommonResponseDto getChatMessageV1(@PathVariable(name = "chatRoomId") Long chatRoomId,
                                              @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                              @RequestParam(value = "limit", required = false, defaultValue = "100") int size
    ) {
        Long memberId = SecurityUtil.getCurrentUserPK().orElseThrow(() -> new NotAuthenticatedException("INVALID ID"));
        Pageable pageable = PageRequest.of(page, size);
        return CommonResponseDto.<ChatMessageResponseDto>builder()
                .data(chatMessageService.findAllMessage(chatRoomId, memberId, pageable))
                .build();
    }


}
