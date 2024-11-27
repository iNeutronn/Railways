package com.railways.railways.domain.client;

public class ClientRedirected {
    private int clientId;
    private int newTicketOfficeId;

    public ClientRedirected(int clientId, int newTicketOfficeId) {
        this.clientId = clientId;
        this.newTicketOfficeId = newTicketOfficeId;
    }

    public int getClientId() {
        return clientId;
    }

    public int getNewTicketOfficeId() {
        return newTicketOfficeId;
    }
}
