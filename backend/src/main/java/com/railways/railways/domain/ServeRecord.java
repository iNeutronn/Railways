package com.railways.railways.domain;

/**
 * A record representing a service event at a ticket office.
 * It stores information about the ticket office, the client, the number of tickets bought,
 * and the start and end times of the service.
 */
public record ServeRecord(
        int ticketOfficeID,
        int clientID,
        int ticketsBought,
        String startTime,
        String endTime
) {}
