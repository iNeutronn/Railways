package com.railways.railways.domain.station;

/**
 * Represents an update to the queue of a specific ticket office.
 * This class is used to store the ticket office ID and the updated queue of client IDs.
 */
public class QueueUpdate {
    private int TicketOfficeId;
    private int[] Queue;

    /**
     * Constructs a new QueueUpdate with the given ticket office ID and queue.
     *
     * @param ticketOfficeId the ID of the ticket office whose queue is being updated
     * @param queue the updated queue of client IDs
     */
    public QueueUpdate(int ticketOfficeId, int[] queue) {
        TicketOfficeId = ticketOfficeId;
        Queue = queue;
    }
}
