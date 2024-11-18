package com.railways.railways.simulator.src.hall;
import javax.swing.text.Position;
import java.util.List;

public class Hall {

    private List<TicketOffice> ticketOffices;
    private List<Position> entrances;
    private TicketOffice reservedTicketOffice;

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

    public void setTicketOffices(List<TicketOffice> ticketOffices) {
        this.ticketOffices = ticketOffices;
    }

    public List<Position> getEntrances() {
        return entrances;
    }

    public void setEntrances(List<Position> entrances) {
        this.entrances = entrances;
    }

    public TicketOffice getReservedTicketOffice() {
        return reservedTicketOffice;
    }

    public void setReservedTicketOffice(TicketOffice reservedTicketOffice) {
        this.reservedTicketOffice = reservedTicketOffice;
    }
}
