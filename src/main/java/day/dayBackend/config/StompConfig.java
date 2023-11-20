package day.dayBackend.config;


import day.dayBackend.config.handler.StompExceptionHandler;
import day.dayBackend.config.handler.StompPreHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompConfig implements WebSocketMessageBrokerConfigurer {

    private final StompPreHandler stompPreHandler;
    private final StompExceptionHandler stompExceptionHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Stomp 웹소켓 엔드포인트 등록
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");

        registry.setErrorHandler(stompExceptionHandler);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지 브로커 구성
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompPreHandler);
    }
}

