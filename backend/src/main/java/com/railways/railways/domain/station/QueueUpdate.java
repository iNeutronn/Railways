package com.railways.railways.domain.station;

public class QueueUpdate {
    private int TicketOfficeId;
    private int[] Queue;

    public QueueUpdate(int ticketOfficeId, int[] queue) {
        TicketOfficeId = ticketOfficeId;
        Queue = queue;
    }
}
