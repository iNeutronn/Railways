package com.railways.railways.listeners;

import com.railways.railways.communication.CommunicationSocketController;
import com.railways.railways.communication.DTO.GenerationUpdateDTO;
import com.railways.railways.communication.DTO.QueueTransferedUpdate;
import com.railways.railways.communication.DTO.QueueUpdatedUpdate;
import com.railways.railways.domain.station.QueueUpdate;
import com.railways.railways.events.QueueTransferedEvent;
import com.railways.railways.events.QueueUpdatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener for handling queue transfer events.
 */
@Component
public class QueueTransferedListener {
    private final CommunicationSocketController socketController;

    /**
     * Constructor to inject the CommunicationSocketController.
     *
     * @param socketController the socket controller to send updates
     */
    public QueueTransferedListener(CommunicationSocketController socketController) {
        this.socketController = socketController;
    }

    /**
     * Handles QueueUpdatedEvent by sending a queue transfer update.
     *
     * @param event the queue update event
     */
    @EventListener
    public void onQueueTransfer(QueueTransferedEvent event) {
        QueueUpdate queueUpdate = event.getQueueUpdate();
        GenerationUpdateDTO update = new QueueTransferedUpdate(queueUpdate);
        socketController.sendUpdate(update);
    }
}

