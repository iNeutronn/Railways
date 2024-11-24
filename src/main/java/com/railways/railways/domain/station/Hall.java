package com.railways.railways.domain.station;
import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.client.ClientCreated;
import com.railways.railways.events.ClientCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Hall {

    private List<TicketOffice> ticketOffices;
    private List<Entrance> entrances;
    private TicketOffice reservedTicketOffice;
    private Segment segment;
    private ApplicationEventPublisher applicationEventPublisher;
    private final Random random;


    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    private Hall() {
        segment = new Segment(new Point(0, 0), new Point(100, 100));
        random = new Random();
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
        Entrance selectedEntrance = selectRandomEntrance();

        var entrancePoint = selectedEntrance.getStartPoint();

        var ticketOffice = getBestTicketOffice(entrancePoint);

        publishClientCreatedEvent(client, selectedEntrance, ticketOffice);

        imitateMoving();

        // Add the client to the nearest ticket office
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

    public TicketOffice getBestTicketOffice(Point entrancePoint) {
        TicketOffice bestOffice = null;
        int minQueueSize = Integer.MAX_VALUE;
        double minDistance = Double.MAX_VALUE;

        for (TicketOffice office : ticketOffices.stream().filter(TicketOffice::isOpen).toList()) {

            int queueSize = office.getQueueSize();
            // Calculate the distance from the client's current position to the starting point of the office's segment
            double clientDistance = entrancePoint.distance(office.getSegment().start);
            if (queueSize < minQueueSize || (queueSize == minQueueSize && clientDistance < minDistance)) {
                minQueueSize = queueSize;
                minDistance = clientDistance;
                bestOffice = office;
            }
        }

        return bestOffice;
    }

    public int closeCashPoint(int id) {
        TicketOffice ticketOffice = ticketOffices
                .stream().filter(office -> office.getOfficeID() == id)
                .findFirst().orElse(null);
        if (ticketOffice == null) {
            return  -1;
        }

        CloseTicketOffice(ticketOffice);

        return id;
    }

    // Removes the ticket office from the list and closes it
    private void CloseTicketOffice(TicketOffice ticketOffice) {
        ticketOffice.closeOffice();
        System.out.println("Hall: Ticket office " + ticketOffice.getOfficeID() + " closed.");
    }

    // Selects a random entrance
    private Entrance selectRandomEntrance() {
        int index = random.nextInt(entrances.size());
        return entrances.get(index);
    }

    // Publishes a ClientCreatedEvent
    private void publishClientCreatedEvent(Client client, Entrance entrance, TicketOffice ticketOffice) {
        ClientCreatedEvent event = new ClientCreatedEvent(
                this,
                new ClientCreated(client, entrance.getId(), ticketOffice.getOfficeID())
        );
        applicationEventPublisher.publishEvent(event);
    }

    // Handles thread sleep with exception handling
    private void imitateMoving() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.err.println("Thread interrupted: " + e.getMessage());
        }
    }

    public Segment getSegment() {
        return segment;
    }
}
