package com.railways.railways.events;

import com.railways.railways.domain.station.QueueUpdate;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class QueueUpdatedEvent extends ApplicationEvent {
    private final QueueUpdate queueUpdate;


    public QueueUpdatedEvent(Object source, QueueUpdate queueUpdate) {
        super(source);
        this.queueUpdate = queueUpdate;

    }

    public QueueUpdate getQueueUpdate() {
        return queueUpdate;
    }
}
