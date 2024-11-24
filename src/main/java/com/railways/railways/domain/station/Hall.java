package com.railways.railways.domain.station;
import com.railways.railways.domain.client.Client;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Hall {

    private List<TicketOffice> ticketOffices;
    private List<Entrance> entrances;
    private TicketOffice reservedTicketOffice;
    private Segment segment;

    private Hall() {
        segment = new Segment(new Point(0, 0), new Point(100, 100));
    }

    private static final class InstanceHolder {
        private static final Hall instance = new Hall();
    }

    public static Hall getInstance() {
        // Double-check locking to ensure thread safety
        return InstanceHolder.instance;
    }

    public List<TicketOffice> getTicketOffices() {
        return ticketOffices;
    }

    public void addTicketOffice(TicketOffice ticketOffice) {
        ticketOffices.add(ticketOffice);
    }

    public void setTicketOffices(List<TicketOffice> ticketOffices) {
        this.ticketOffices = ticketOffices;
    }

    public List<Entrance> getEntrances() {
        return entrances;
    }

    public void setEntrances(List<Entrance> entrances) {
        this.entrances = entrances;
    }

    public TicketOffice getReservedTicketOffice() {
        return reservedTicketOffice;
    }

    public void setReservedTicketOffice(TicketOffice reservedTicketOffice) {
        this.reservedTicketOffice = reservedTicketOffice;
    }

    public void processClient(Client client) {
        int index = new Random().nextInt(entrances.size());
        System.out.println(index);
        var entrancePoint = entrances.get(index).getStartPoint();
        client.setPosition(entrancePoint);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Add the client to the nearest ticket office
        var ticketOffice = getBestTicketOffice(client);
        ticketOffice.addClient(client);
        System.out.println("Hall: Client " + client.getFullName() + " added to ticket office " + ticketOffice.getOfficeID());
    }

    public  int getClientCount() {
        int count = 0;
        for (TicketOffice ticketOffice : ticketOffices) {
            count += ticketOffice.getQueueSize();
        }
        return count;
    }

    public TicketOffice getBestTicketOffice(Client client) {
        TicketOffice bestOffice = null;
        int minQueueSize = Integer.MAX_VALUE;
        double minDistance = Double.MAX_VALUE;

        for (TicketOffice office : ticketOffices) {

            int queueSize = office.getQueueSize();
            // Calculate the distance from the client's current position to the starting point of the office's segment
            double clientDistance = client.getPosition().distance(office.getSegment().start);
            if (queueSize < minQueueSize || (queueSize == minQueueSize && clientDistance < minDistance)) {
                minQueueSize = queueSize;
                minDistance = clientDistance;
                bestOffice = office;
            }
        }

        return bestOffice;
    }

    public Segment getSegment() {
        return segment;
    }
}
