package com.railways.railways.simulator.src.hall;

import java.awt.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

public class TicketOffice implements Runnable {
    private final int ticketOfficeID;
    private PriorityQueue<Client> clientsQueue;
    private ArrayList<ServeRecord> serveRecords;
    private Segment location;
    private boolean isOpen;

    private final int minServiceTime;
    private final int maxServiceTime;

    public TicketOffice(int ticketOfficeID, Segment position, int minServiceTime, int maxServiceTime) {
        this.ticketOfficeID = ticketOfficeID;
        // Using a PriorityQueue with a comparator that prioritizes clients based on their privilege
        this.clientsQueue = new PriorityQueue<>((c1, c2) -> {
            int priorityComparison = Integer.compare(c2.getPriority(), c1.getPriority());
            return priorityComparison != 0 ? priorityComparison : Long.compare(c1.getTimestamp(), c2.getTimestamp());
        });

        this.serveRecords = new ArrayList<>();
        this.location = position;
        this.isOpen = true;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;
    }

    @Override
    public void run() {
        while (isOpen) {
            serveClient();
            try {
                // Short pause to simulate time between clients
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void serveClient() {
        if (clientsQueue.isEmpty()) return;

        Client client = clientsQueue.poll();
        if (client == null) return;

        int serviceTime = getRandomServeTime();

        // Simulate serving the client
        try {
            Thread.sleep((long) serviceTime * client.getTicketsToBuy());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        ServeRecord record = new ServeRecord(ticketOfficeID, client.getClientID(), client.getTicketsToBuy(), serviceTime);
        serveRecords.add(record);

        //TODO: Notify ui that they have been served
    }

    private int getRandomServeTime() {
        Random random = new Random();
        return random.nextInt(maxServiceTime - minServiceTime + 1) + minServiceTime;
    }

    public void addClient(Client client) {
        clientsQueue.add(client);
    }

    public void closeOffice() {
        isOpen = false;
    }

    public ArrayList<ServeRecord> getServeRecords() {
        return serveRecords;
    }

    public Point getFrontPosition()
    {
        // Return the position between the top left and top right corners as the front position of the ticket office
        // TODO: Find optimal solution for this
        int x = location.getBottomRight().x - location.getTopLeft().x;
        int y = location.getTopLeft().y;
        return new Point(x, y);
    }

    public Point getQueueEndPosition() {
        // Return the position of the last client in the queue
        if (clientsQueue.isEmpty()) return getFrontPosition();
        ArrayList<Client> list = new ArrayList<>(clientsQueue);
        return list.getLast().getPosition();
    }
}
