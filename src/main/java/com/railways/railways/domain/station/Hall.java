package com.railways.railways.domain.station;
import com.railways.railways.domain.MapManager;
import com.railways.railways.domain.client.Client;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Hall {

    private List<TicketOffice> ticketOffices;
    private List<Segment> entrances;
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

    public List<Segment> getEntrances() {
        return entrances;
    }

    public void setEntrances(List<Segment> entrances) {
        this.entrances = entrances;
    }

    public TicketOffice getReservedTicketOffice() {
        return reservedTicketOffice;
    }

    public void setReservedTicketOffice(TicketOffice reservedTicketOffice) {
        this.reservedTicketOffice = reservedTicketOffice;
    }

    public void addClient(Client client) {
        int index = new Random().nextInt(entrances.size());
//        System.out.println(index);
        var entrancePoint = entrances.get(index).start;
        client.setPosition(entrancePoint);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Add the client to the nearest ticket office
        var ticketOffice = MapManager.getClosestTicketOffice(client, ticketOffices);
        ticketOffice.addClient(client);
//        System.out.println("Hall: Client " + client.getFullName() + " added to ticket office " + ticketOffice.getOfficeID());
    }

    public  int getClientCount() {
        int count = 0;
        for (TicketOffice ticketOffice : ticketOffices) {
            count += ticketOffice.getQueueSize();
        }
        return count;
    }

    public Segment getSegment() {
        return segment;
    }
}
