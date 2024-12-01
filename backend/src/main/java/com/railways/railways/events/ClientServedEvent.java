package com.railways.railways.events;


import com.railways.railways.domain.ServeRecord;
import org.springframework.context.ApplicationEvent;

/**
 * Event triggered when a client is served by a ticket office.
 */
public class ClientServedEvent extends ApplicationEvent {
    private final ServeRecord serveRecord;

    /**
     * Constructs a ClientServedEvent.
     *
     * @param source      the object that published the event (usually 'this')
     * @param serveRecord details of the service record for the served client
     */
    public ClientServedEvent(Object source, ServeRecord serveRecord) {
        super(source);
        this.serveRecord = serveRecord;
    }

    /**
     * Retrieves the service record associated with this event.
     *
     * @return the service record
     */
    public ServeRecord getServeRecord() {
        return serveRecord;
    }

}
