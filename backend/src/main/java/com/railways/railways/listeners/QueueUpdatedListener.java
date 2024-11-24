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

@Component
public class QueueUpdatedListener {
    private final CommunicationSocketController socketController;

    public QueueUpdatedListener(CommunicationSocketController socketController) {
        this.socketController = socketController;
    }

    @EventListener
    public void onQueueUpdate(QueueUpdatedEvent event) {
        QueueUpdate queueUpdate = event.getQueueUpdate();
        GenerationUpdateDTO update = new QueueUpdatedUpdate(queueUpdate);
        socketController.sendUpdate(update);
    }
}
