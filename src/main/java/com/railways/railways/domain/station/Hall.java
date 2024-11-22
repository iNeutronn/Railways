package com.railways.railways.domain.station;
import com.railways.railways.domain.client.Client;

import java.util.List;

public class Hall {

    private List<TicketOffice> ticketOffices;
    private List<Segment> entrances;
    private TicketOffice reservedTicketOffice;
    private Segment segment;

    private Hall() {

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
//        reservedTicketOffice.addClient(client);
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
