package com.railways.railways.Configuration;
import com.railways.railways.communication.CommunicationSocketController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocketConfig configures WebSocket support for the railway system.
 * This class registers WebSocket handlers to handle communication over WebSocket.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    // The CommunicationSocketController to handle WebSocket connections
    private final CommunicationSocketController communicationSocketController;

    /**
     * Constructs a WebSocketConfig with the provided CommunicationSocketController.
     *
     * @param communicationSocketController the controller that handles WebSocket communication
     */
    public WebSocketConfig(CommunicationSocketController communicationSocketController) {
        this.communicationSocketController = communicationSocketController;
    }

    /**
     * Registers WebSocket handlers to the WebSocketHandlerRegistry.
     * This method maps the "/ws" URL to the communicationSocketController and
     * configures it to allow connections from any origin (this can be restricted if needed).
     *
     * @param registry the WebSocketHandlerRegistry used to register WebSocket handlers
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(communicationSocketController, "/ws")
                .setAllowedOrigins("*"); // Allow all origins or restrict as needed
    }
}