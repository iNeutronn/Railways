package com.railways.railways;
import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.client.PrivilegeEnum;
import com.railways.railways.domain.station.Direction;
import com.railways.railways.domain.station.Segment;
import com.railways.railways.domain.station.TicketOffice;
import com.railways.railways.events.QueueUpdatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;

import java.awt.*;
import java.util.concurrent.PriorityBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

public class TicketOfficeTest {
    private TicketOffice ticketOffice;
    private ApplicationEventPublisher eventPublisher;

    @BeforeEach
    public void setup() {
        eventPublisher = Mockito.mock(ApplicationEventPublisher.class);
        Segment segment = new Segment(new Point(0, 0), new Point(10, 10));
        ticketOffice = new TicketOffice(eventPublisher, 1, segment, Direction.Up, 100, 200);
    }

    @Test
    public void testAddClient() {
        Client client = new Client(1, "John", "Doe", 2, PrivilegeEnum.DEFAULT);
        ticketOffice.addClient(client);

        assertEquals(1, ticketOffice.getQueueSize());
        Mockito.verify(eventPublisher, Mockito.times(1)).publishEvent(Mockito.any(QueueUpdatedEvent.class));
    }

    @Test
    public void testServeClient() throws InterruptedException {
        Client client = new Client(1, "John", "Doe", 2, PrivilegeEnum.DEFAULT);
        ticketOffice.addClient(client);

        ticketOffice.serveClient();

        assertEquals(0, ticketOffice.getQueueSize());
        assertEquals(1, ticketOffice.getServeRecords().size());
    }

    @Test
    public void testOpenAndCloseOffice() {
        ticketOffice.closeOffice();
        assertFalse(ticketOffice.isOpen());

        ticketOffice.openOffice();
        assertTrue(ticketOffice.isOpen());
    }

    @Test
    public void testClearQueue() {
        ticketOffice.addClient(new Client(1, "John", "Doe", 2, PrivilegeEnum.DEFAULT));
        ticketOffice.clearQueue();

        assertEquals(0, ticketOffice.getQueueSize());
    }
}
