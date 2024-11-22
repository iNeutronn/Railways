package com.railways.railways.eventLisinerExpample;

import com.railways.railways.events.ClientCreatedEvent;
import org.springframework.context.event.EventListener;

public class WebSocketEventListener2 {

    @EventListener
    public void onApplicationEvent(ClientCreatedEvent event) {
        System.out.println("Received spring custom event - " + event.getCreatedClient().getFirstName());
    }
}
