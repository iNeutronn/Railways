package com.railways.railways.communication;

import com.google.gson.Gson;
import com.railways.railways.communication.DTO.GenerationUpdateDTO;
import com.railways.railways.logging.LogLevel;
import com.railways.railways.logging.Logger;
import com.railways.railways.simulation.SimulationService;
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
    private final SimulationService simulationService;

    private final Logger logger;

    public CommunicationSocketController(SimulationService simulationService, Logger logger) {
        this.simulationService = simulationService;
        this.logger = logger;
    }
    /**
     * Called after a new WebSocket connection is established.
     *
     * @param session the WebSocket session
     */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        sessions.add(session);
        logger.log("New WebSocket connection established: " + session.getId(), LogLevel.Info);
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
        logger.log("WebSocket connection closed: " + session.getId() +
                ", status: " + status.toString(), LogLevel.Info);
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
            logger.log("Error sending message: " + e.getMessage(), LogLevel.Error);
        }
    }

    /**
     * Resumes the simulation by calling the start method of the simulation service.
     */
    public void resumeSimulation() {
        simulationService.startSimulation();
        logger.log("Simulation resumed by WebSocket command.", LogLevel.Info);
    }

    /**
     * Pauses the simulation by calling the stop method of the simulation service.
     */
    public void pauseSimulation() {
        simulationService.stopSimulation();
        logger.log("Simulation paused by WebSocket command.", LogLevel.Info);
    }
}