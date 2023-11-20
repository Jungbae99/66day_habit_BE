package day.dayBackend.config.handler;

import day.dayBackend.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StompPreHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        StompCommand stompCommand = headerAccessor.getCommand();

        if (stompCommand == null) {
            throw new IllegalArgumentException("Unknown StompCommand");
        }

        if (StompCommand.CONNECT == headerAccessor.getCommand()) {
            handleConnect(headerAccessor);
        }

        return message;
    }

    private void handleConnect(StompHeaderAccessor headerAccessor) {
        List<String> authorizationHeader = headerAccessor.getNativeHeader("Authorization");

        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            throw new AccessDeniedException("Authorization 헤더가 누락되었습니다.");
        }

        String token = authorizationHeader.get(0).substring(7);
        tokenProvider.validateTokenForStomp(token);
    }
}
