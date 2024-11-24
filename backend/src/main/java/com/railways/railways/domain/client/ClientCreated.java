package com.railways.railways.domain.client;

public class ClientCreated {
    private Client client;
    private  int entranceId;
    private  int ticketOfficeId;
    private double time;

    public ClientCreated(Client client, int entranceId, int ticketOfficeId, double time) {
        this.client = client;
        this.entranceId = entranceId;
        this.ticketOfficeId = ticketOfficeId;
        this.time = time;
    }
}
