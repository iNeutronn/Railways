package com.railways.railways.listeners;

import com.railways.railways.communication.CommunicationSocketController;
import com.railways.railways.communication.DTO.ClientServedUpdate;
import com.railways.railways.communication.DTO.GenerationUpdateDTO;
import com.railways.railways.communication.DTO.QueueUpdatedUpdate;
import com.railways.railways.domain.ServeRecord;
import com.railways.railways.domain.station.QueueUpdate;
import com.railways.railways.events.ClientServedEvent;
import com.railways.railways.events.QueueUpdatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener for handling queue updated events.
 */
@Component
public class QueueUpdatedListener {
    private final CommunicationSocketController socketController;

    /**
     * Constructor for injecting the CommunicationSocketController dependency.
     *
     * @param socketController the socket controller used for sending updates
     */
    public QueueUpdatedListener(CommunicationSocketController socketController) {
        this.socketController = socketController;
    }

    /**
     * Listens to QueueUpdatedEvent and sends queue updates via the socket controller.
     *
     * @param event the queue updated event
     */
    @EventListener
    public void onQueueUpdate(QueueUpdatedEvent event) {
        QueueUpdate queueUpdate = event.getQueueUpdate();
        GenerationUpdateDTO update = new QueueUpdatedUpdate(queueUpdate);
        socketController.sendUpdate(update);
    }
}
