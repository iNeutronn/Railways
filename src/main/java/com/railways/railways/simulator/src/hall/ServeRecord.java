package com.railways.railways.simulator.src.hall;

import java.time.Duration;


public record ServeRecord(
        int ticketOfficeID,
        int clientID,
        int ticketsBought,
        Duration timeSpent
) {}
