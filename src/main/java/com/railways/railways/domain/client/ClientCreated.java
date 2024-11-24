package com.railways.railways.domain.client;

public class ClientCreated {
    private Client client;
    private  int entranceId;
    private  int ticketOfficeId;

    public ClientCreated(Client client, int entranceId, int ticketOfficeId) {
        this.client = client;
        this.entranceId = entranceId;
        this.ticketOfficeId = ticketOfficeId;
    }
}
