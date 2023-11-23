package day.dayBackend.controller;

import day.dayBackend.dto.request.chat.MessageRequestDto;
import day.dayBackend.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Slf4j
@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
public class StompController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ChatRoomService chatService;

    @MessageMapping("/chat")
    public void message(MessageRequestDto message) {
        simpMessageSendingOperations.convertAndSend("/sub/channel/" + message.getReceiverId(), message);
        chatService.chatRoomSave(message);
    }
}