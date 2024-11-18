package com.railways.railways.simulator.src.hall;


public record ServeRecord(
        int ticketOfficeID,
        int clientID,
        int ticketsBought,
        int timeSpent
) {}
