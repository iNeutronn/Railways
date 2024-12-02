package com.railways.railways;

import com.google.gson.Gson;
import com.railways.railways.communication.CommunicationSocketController;
import com.railways.railways.communication.DTO.ClientReachedQueue;
import com.railways.railways.communication.DTO.GenerationUpdateDTO;
import com.railways.railways.communication.DTO.GenerationUpdateTypes;
import com.railways.railways.logging.LogLevel;
import com.railways.railways.logging.Logger;
import com.railways.railways.simulation.SimulationService;
import com.railways.railways.domain.station.Hall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommunicationSocketControllerTest {
    private CommunicationSocketController controller;
    private SimulationService simulationServiceMock;
    private Logger loggerMock;
    private Gson gson;

    @BeforeEach
    void setUp() {
        simulationServiceMock = mock(SimulationService.class);
        loggerMock = mock(Logger.class);
        controller = new CommunicationSocketController(simulationServiceMock, loggerMock);
        gson = new Gson();
    }

    @Test
    void testAfterConnectionEstablished() throws Exception {
        WebSocketSession sessionMock = mock(WebSocketSession.class);
        when(sessionMock.getId()).thenReturn("session1");

        controller.afterConnectionEstablished(sessionMock);

        verify(loggerMock).log("New WebSocket connection established: session1", LogLevel.Info);
    }

    @Test
    void testAfterConnectionClosed() throws Exception {
        WebSocketSession sessionMock = mock(WebSocketSession.class);
        CloseStatus closeStatus = CloseStatus.NORMAL;

        when(sessionMock.getId()).thenReturn("session1");

        controller.afterConnectionEstablished(sessionMock);
        controller.afterConnectionClosed(sessionMock, closeStatus);

        verify(loggerMock).log("WebSocket connection closed: session1, status: " + closeStatus, LogLevel.Info);
    }

    @Test
    void testSendUpdate() throws Exception {
        WebSocketSession sessionMock = mock(WebSocketSession.class);

        // Дані для тесту GenerationUpdateDTO
        ClientReachedQueue clientReachedQueue = new ClientReachedQueue(123, 5);
        GenerationUpdateDTO update = new GenerationUpdateDTO(/* передайте дані для тесту */) {
            // Тестовий клас для GenerationUpdateDTO
            public GenerationUpdateTypes type = GenerationUpdateTypes.QueueUpdated;
            public Object data = clientReachedQueue;
        };

        when(sessionMock.isOpen()).thenReturn(true);
        controller.afterConnectionEstablished(sessionMock);

        controller.sendUpdate(update);

        verify(sessionMock).sendMessage(any(TextMessage.class));
        verify(loggerMock, never()).log(contains("Error sending message"), eq(LogLevel.Error));
    }

    @Test
    void testHandleTextMessage() throws Exception {
        // Мокнемо WebSocket сесію
        WebSocketSession sessionMock = mock(WebSocketSession.class);

        // Створюємо тестові дані для ClientReachedQueue
        ClientReachedQueue update = new ClientReachedQueue(123, 5);

        // Серіалізуємо об'єкт в JSON
        String messagePayload = gson.toJson(update);
        TextMessage message = new TextMessage(messagePayload);

        // Мокнемо Hall, який повертатиметься з getHall
        Hall hallMock = mock(Hall.class);
        when(simulationServiceMock.getHall()).thenReturn(hallMock);

        // Викликаємо метод handleTextMessage
        controller.handleTextMessage(sessionMock, message);

        // Перевірка, чи викликається clientReachedCashPoint з правильними параметрами
        verify(hallMock, times(1))
                .clientReachedCashPoint(update.getClientId(), update.getQueueId());

        // Перевірка, що логування не містить помилок
        verify(loggerMock, never()).log(contains("Error processing message"), eq(LogLevel.Error));
    }

    @Test
    void testResumeSimulation() {
        controller.resumeSimulation();

        verify(simulationServiceMock).startSimulation();
        verify(loggerMock).log("Simulation resumed by WebSocket command.", LogLevel.Info);
    }

    @Test
    void testPauseSimulation() {
        controller.pauseSimulation();

        verify(simulationServiceMock).stopSimulation();
        verify(loggerMock).log("Simulation paused by WebSocket command.", LogLevel.Info);
    }
}
