package com.railways.railways.eventLisinerExpample;

import com.railways.railways.events.ClientCreatedEvent;
import org.springframework.context.ApplicationListener;


public class WebSocketEventListener implements ApplicationListener<ClientCreatedEvent> {
    @Override
    public void onApplicationEvent(ClientCreatedEvent event) {
        System.out.println("Received spring custom event - " + event.getCreatedClient().getFirstName());
    }
}
