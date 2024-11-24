package com.railways.railways.events;

import com.railways.railways.domain.station.QueueUpdate;
import org.springframework.context.ApplicationEvent;

public class QueueTransferedEvent extends ApplicationEvent {
    private final QueueUpdate queueUpdate;


    public QueueTransferedEvent(Object source, QueueUpdate queueUpdate) {
        super(source);
        this.queueUpdate = queueUpdate;

    }

    public QueueUpdate getQueueUpdate() {
        return queueUpdate;
    }
}
