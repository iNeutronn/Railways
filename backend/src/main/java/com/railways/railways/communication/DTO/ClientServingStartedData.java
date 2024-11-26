package com.railways.railways.communication.DTO;

public class ClientServingStartedData {
    private final int ticketOfficeId;
    private final int clientId;
    private final double timeNeeded;

    public ClientServingStartedData(int ticketOfficeId, int clientId, double timeNeeded) {
        this.ticketOfficeId = ticketOfficeId;
        this.clientId = clientId;
        this.timeNeeded = timeNeeded;
    }

    public int getTicketOfficeId() {
        return ticketOfficeId;
    }

    public int getClientId() {
        return clientId;
    }

    public double getTimeNeeded() {
        return timeNeeded;
    }
}
