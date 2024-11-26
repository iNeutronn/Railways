package com.railways.railways.events;

import com.railways.railways.communication.DTO.ClientServingStartedData;
import org.springframework.context.ApplicationEvent;

public class ClientServingStartedEvent  extends ApplicationEvent {
    private ClientServingStartedData clientServingStartedData;

    public ClientServingStartedEvent(Object source, ClientServingStartedData clientServingStartedData) {
        super(source);
        this.clientServingStartedData = clientServingStartedData;
    }
    public ClientServingStartedData getClientServingStartedData() {
        return clientServingStartedData;
    }
}
