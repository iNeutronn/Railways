package com.railways.railways;

import com.railways.railways.communication.CommunicationSocketController;
import com.railways.railways.communication.DTO.ClientServedUpdate;
import com.railways.railways.domain.ServeRecord;
import com.railways.railways.events.ClientServedEvent;
import com.railways.railways.listeners.ClientServedListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientServedListenerTest {

    @Mock
    private CommunicationSocketController mockSocketController;

    private ClientServedListener listener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        listener = new ClientServedListener(mockSocketController);
    }

    @Test
    void testOnClientServed() {
        // Arrange
        ServeRecord serveRecord = new ServeRecord(/* Initialize ServeRecord */);
        ClientServedEvent event = new ClientServedEvent(this, serveRecord);

        // Act
        listener.onClientServed(event);

        // Assert
        ArgumentCaptor<ClientServedUpdate> captor = ArgumentCaptor.forClass(ClientServedUpdate.class);
        verify(mockSocketController).sendUpdate(captor.capture());
        assertTrue(captor.getValue() instanceof ClientServedUpdate);
    }
}
