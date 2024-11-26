package com.railways.railways.domain.client;

/**
 * Represents an event where a client is created, along with the associated entrance and ticket office information.
 * The event also records the time at which the client was created.
 */
public class ClientCreated {
    // The client associated with this event
    private Client client;
    // The ID of the entrance where the client is located
    private  int entranceId;
    // The ID of the ticket office associated with this event
    private  int ticketOfficeId;
    // The time when the client was created (in some time unit, e.g., seconds)
    private double time;

    /**
     * Constructs a ClientCreated event with the given details.
     *
     * @param client the client that was created
     * @param entranceId the ID of the entrance where the client is located
     * @param ticketOfficeId the ID of the ticket office associated with the event
     * @param time the time when the client was created
     */
    public ClientCreated(Client client, int entranceId, int ticketOfficeId, double time) {
        this.client = client;
        this.entranceId = entranceId;
        this.ticketOfficeId = ticketOfficeId;
        this.time = time;
    }
}
