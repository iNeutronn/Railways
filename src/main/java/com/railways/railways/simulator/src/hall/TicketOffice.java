package com.railways.railways.simulator.src.hall;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

public class TicketOffice implements Runnable {
    private final int ticketOfficeID;
    private final PriorityBlockingQueue<Client> clientsQueue;
    private final ArrayList<ServeRecord> serveRecords;
    private final Segment location;
    private boolean isOpen;

    private final int minServiceTime;
    private final int maxServiceTime;

    public TicketOffice(int ticketOfficeID, Segment position, int minServiceTime, int maxServiceTime) {
        this.ticketOfficeID = ticketOfficeID;

        this.clientsQueue = new PriorityBlockingQueue<>(
                10,
                (c1, c2) -> {
                    int priorityComparison = Integer.compare(c2.getPriority(), c1.getPriority());
                    return priorityComparison != 0 ? priorityComparison : Long.compare(c1.getTimestamp(), c2.getTimestamp());
                }
        );

        this.serveRecords = new ArrayList<>();
        this.location = position;
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

        // TODO: Notify the UI that the client has been served
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

    public Point getFrontPosition() {
        // Return the position between the top left and top right corners as the front position of the ticket office
        // TODO: introduce direction to get right position
        int x = location.getBottomRight().x - location.getTopLeft().x;
        int y = location.getTopLeft().y;
        return new Point(x, y);
    }

    public Point getQueueEndPosition() {
        ArrayList<Client> clientList = new ArrayList<>(clientsQueue);

        if (clientList.isEmpty()) {
            return getFrontPosition();
        }

        Client lastClient = clientList.getLast();
        return lastClient.getPosition();
    }
}
