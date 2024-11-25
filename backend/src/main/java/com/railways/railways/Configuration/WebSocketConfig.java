package com.railways.railways.Configuration;
import com.railways.railways.communication.CommunicationSocketController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {


    private final CommunicationSocketController communicationSocketController;

    public WebSocketConfig(CommunicationSocketController communicationSocketController) {
        this.communicationSocketController = communicationSocketController;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(communicationSocketController, "/ws")
                .setAllowedOrigins("*"); // Allow all origins or restrict as needed
    }
}