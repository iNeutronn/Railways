package com.railways.railways.communication;

import com.google.gson.Gson;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * CommunicationSocketController is a WebSocket handler that manages WebSocket sessions
 * and facilitates sending updates to connected clients.
 */
@Component
public class CommunicationSocketController extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new ArrayList<>();
    private final Gson gson = new Gson(); // Gson initialization

    /**
     * Called after a new WebSocket connection is established.
     *
     * @param session the WebSocket session
     */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        sessions.add(session);
        System.out.println("New connection established: " + session.getId());
    }

    /**
     * Called after a WebSocket connection is closed.
     *
     * @param session the WebSocket session
     * @param status the status of the closed connection
     */
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        sessions.remove(session);
        System.out.println("Connection closed: " + session.getId());
    }

    /**
     * Sends an update to all connected WebSocket clients.
     *
     * @param update the update to be sent
     */
    public void sendUpdate(@NonNull GenerationUpdateDTO update) {
        try {
            String jsonMessage = gson.toJson(update);
            TextMessage message = new TextMessage(jsonMessage);
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            }
        } catch (Exception e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }
}