package com.railways.railways.communication.DTO;

public class ClientReachedQueue  {
    private int clientId;
    private int queueId;

    public ClientReachedQueue(int clientId, int queueId) {
        this.clientId = clientId;
        this.queueId = queueId;
    }

    public int getClientId() {
        return clientId;
    }

    public int getQueueId() {
        return queueId;
    }
}
