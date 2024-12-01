package com.railways.railways.events;

import com.railways.railways.domain.station.QueueUpdate;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * Event triggered when a queue is updated.
 */
public class QueueUpdatedEvent extends ApplicationEvent {
    private final QueueUpdate queueUpdate;

    /**
     * Constructs a QueueUpdatedEvent.
     *
     * @param source      the object that published the event
     * @param queueUpdate the queue update details
     */
    public QueueUpdatedEvent(Object source, QueueUpdate queueUpdate) {
        super(source);
        this.queueUpdate = queueUpdate;

    }

    /**
     * Retrieves the queue update associated with this event.
     *
     * @return the queue update
     */
    public QueueUpdate getQueueUpdate() {
        return queueUpdate;
    }
}
