package com.railways.railways.events;


import com.railways.railways.domain.ServeRecord;
import org.springframework.context.ApplicationEvent;

public class ClientServedEvent extends ApplicationEvent {
    private final ServeRecord serveRecord;


    public ClientServedEvent(Object source, ServeRecord serveRecord) {
        super(source);
        this.serveRecord = serveRecord;
    }

    public ServeRecord getServeRecord() {
        return serveRecord;
    }

}
