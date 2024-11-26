package com.railways.railways.events;

import com.railways.railways.domain.station.QueueUpdate;
import org.springframework.context.ApplicationEvent;

/**
 * Event triggered when a queue is transferred or updated.
 */
public class QueueTransferedEvent extends ApplicationEvent {
    private final QueueUpdate queueUpdate;

    /**
     * Constructs a QueueTransferedEvent.
     *
     * @param source      the object that published the event
     * @param queueUpdate the queue update details
     */
    public QueueTransferedEvent(Object source, QueueUpdate queueUpdate) {
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
