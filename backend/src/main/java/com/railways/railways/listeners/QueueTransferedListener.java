package com.railways.railways.listeners;

import com.railways.railways.communication.CommunicationSocketController;
import com.railways.railways.communication.DTO.GenerationUpdateDTO;
import com.railways.railways.communication.DTO.QueueTransferedUpdate;
import com.railways.railways.communication.DTO.QueueUpdatedUpdate;
import com.railways.railways.domain.station.QueueUpdate;
import com.railways.railways.events.QueueUpdatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class QueueTransferedListener {
    private final CommunicationSocketController socketController;

    public QueueTransferedListener(CommunicationSocketController socketController) {
        this.socketController = socketController;
    }

    @EventListener
    public void onQueueTransfer(QueueUpdatedEvent event) {
        QueueUpdate queueUpdate = event.getQueueUpdate();
        GenerationUpdateDTO update = new QueueTransferedUpdate(queueUpdate);
        socketController.sendUpdate(update);
    }
}

