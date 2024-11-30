package com.railways.railways;
import com.railways.railways.communication.CommunicationSocketController;
import com.railways.railways.communication.DTO.ClientCreatedUpdate;
import com.railways.railways.domain.client.ClientCreated;
import com.railways.railways.events.ClientCreatedEvent;
import com.railways.railways.listeners.ClientCreatedListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientCreatedListenerTest {

    @Mock
    private CommunicationSocketController mockSocketController;

    private ClientCreatedListener listener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        listener = new ClientCreatedListener(mockSocketController);
    }

    @Test
    void testOnClientCreated() {
        // Arrange
        ClientCreated clientCreated = new ClientCreated(/* Initialize ClientCreated */);
        ClientCreatedEvent event = new ClientCreatedEvent(this, clientCreated);

        // Act
        listener.onClientCreated(event);

        // Assert
        ArgumentCaptor<ClientCreatedUpdate> captor = ArgumentCaptor.forClass(ClientCreatedUpdate.class);
        verify(mockSocketController).sendUpdate(captor.capture());
        assertTrue(captor.getValue() instanceof ClientCreatedUpdate);
    }
}
