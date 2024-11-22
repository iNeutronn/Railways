package com.railways.railways.domain.station;

import com.railways.railways.domain.ServeRecord;
import com.railways.railways.domain.client.Client;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

public class TicketOffice implements Runnable {
    private final int ticketOfficeID;
    private final PriorityBlockingQueue<Client> clientsQueue;
    private final ArrayList<ServeRecord> serveRecords;
    private boolean isOpen;
    private Segment segment;
    private Direction direction;

    private final int minServiceTime;
    private final int maxServiceTime;

    public TicketOffice(int ticketOfficeID, Segment position, Direction direction, int minServiceTime, int maxServiceTime) {
        this.ticketOfficeID = ticketOfficeID;
        this.direction = direction;
        this.segment = position;

        this.clientsQueue = new PriorityBlockingQueue<>(
                10,
                (c1, c2) -> {
                    int priorityComparison = Integer.compare(c2.getPriority(), c1.getPriority());
                    return priorityComparison != 0 ? priorityComparison : Long.compare(c1.getTimestamp(), c2.getTimestamp());
                }
        );

        this.serveRecords = new ArrayList<>();
        this.isOpen = true;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;
    }

    @Override
    public void run() {
        while (isOpen) {
            try {
                serveClient();
                Thread.sleep(100); // Short pause between clients
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void serveClient() throws InterruptedException {
        Client client = clientsQueue.take();

        int serviceTime = getRandomServeTime();
        try {
            // Simulate serving the client
            Thread.sleep((long) serviceTime * client.getTicketsToBuy());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        ServeRecord record = new ServeRecord(ticketOfficeID, client.getClientID(), client.getTicketsToBuy(), serviceTime);
        serveRecords.add(record);
        System.out.println("TicketOffice: Client " + client.getFullName() + " with id:" + client.getClientID() + " served by ticket office " + ticketOfficeID);

        // TODO: Notify the UI that the client has been served
    }

    public int getOfficeID() {
        return ticketOfficeID;
    }

    private int getRandomServeTime() {
        Random random = new Random();
        return random.nextInt(maxServiceTime - minServiceTime + 1) + minServiceTime;
    }

    public void addClient(Client client) {
        clientsQueue.offer(client); // Thread-safe addition
    }

    public void closeOffice() {
        isOpen = false;
    }

    public ArrayList<ServeRecord> getServeRecords() {
        return serveRecords;
    }

    public int getQueueSize() {
        return clientsQueue.size();
    }

    public Segment getSegment() {
        return segment;
    }

    public Direction getDirection() {
        return direction;
    }

    public PriorityBlockingQueue<Client> getQueue() {
        return clientsQueue;
    }
}
