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
 * The {@code CommunicationSocketController} is a WebSocket handler responsible for managing
 * WebSocket sessions and sending updates to all connected clients.
 *
 * This component handles WebSocket connections, maintains active sessions, and allows
 * for sending real-time updates to the clients. It is also responsible for controlling
 * the simulation state (resuming and pausing) via WebSocket commands.
 *
 * @see TextWebSocketHandler
 * @see WebSocketSession
 * @see GenerationUpdateDTO
 */
@Component
public class CommunicationSocketController extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new ArrayList<>();
    private final Gson gson = new Gson(); // Gson initialization
    private final SimulationService simulationService;
    private final Logger logger;

    /**
     * Constructs a {@code CommunicationSocketController} with the provided services.
     *
     * @param simulationService the service that manages the simulation logic
     * @param logger the logger for logging communication events
     */
    public CommunicationSocketController(SimulationService simulationService, Logger logger) {
        this.simulationService = simulationService;
        this.logger = logger;
    }
    /**
     * Called when a new WebSocket connection is established.
     *
     * This method adds the new session to the list of active sessions and logs the connection event.
     *
     * @param session the WebSocket session representing the new connection
     */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        sessions.add(session);
        logger.log("New WebSocket connection established: " + session.getId(), LogLevel.Info);
    }

    /**
     * Called when a WebSocket connection is closed.
     *
     * This method removes the session from the list of active sessions and logs the disconnection event.
     *
     * @param session the WebSocket session representing the closed connection
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
     * The update is serialized into a JSON message using Gson, and then sent to all open sessions.
     *
     * @param update the update to be sent to all clients
     * @throws IllegalArgumentException if the update cannot be serialized
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
     * Resumes the simulation by calling the start method of the {@link SimulationService}.
     *
     * This method allows the simulation to resume when triggered by a WebSocket command.
     */
    public void resumeSimulation() {
        simulationService.startSimulation();
        logger.log("Simulation resumed by WebSocket command.", LogLevel.Info);
    }

    /**
     * Pauses the simulation by calling the stop method of the {@link SimulationService}.
     *
     * This method allows the simulation to pause when triggered by a WebSocket command.
     */
    public void pauseSimulation() {
        simulationService.stopSimulation();
        logger.log("Simulation paused by WebSocket command.", LogLevel.Info);
    }
}