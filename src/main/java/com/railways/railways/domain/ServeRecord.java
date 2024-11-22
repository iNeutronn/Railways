package com.railways.railways.domain;


public record ServeRecord(
        int ticketOfficeID,
        int clientID,
        int ticketsBought,
        int timeSpent
) {}
