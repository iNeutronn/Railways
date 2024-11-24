package com.railways.railways.domain.station;
import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.client.ClientCreated;
import com.railways.railways.events.ClientCreatedEvent;
import com.railways.railways.events.QueueTransferedEvent;
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
    private double moveSpeed = 100.0;


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

    public void setMoveSpeed(double moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public void processClient(Client client) {
        Entrance selectedEntrance = selectRandomEntrance();

        var entrancePoint = selectedEntrance.getStartPoint();

        var ticketOffice = getBestTicketOffice(entrancePoint);


        var distance = entrancePoint.distance(ticketOffice.getSegment().start);
        distance -= ticketOffice.getQueueSize();
        var time = (double) (distance * moveSpeed);
        publishClientCreatedEvent(client, selectedEntrance, ticketOffice,time);


        if (distance < 0) {
            distance = 1;
        }
        imitateMoving((long) time);

        // Add the client to the nearest ticket office
        ticketOffice.addClient(client);
        System.out.println("Hall: Client " + client.getFullName()
                + " added to ticket office " + ticketOffice.getOfficeID()
                + " time to move: " + distance * moveSpeed);
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

    public int closeTicketOffice(int id) {
        TicketOffice ticketOffice = ticketOffices
                .stream().filter(office -> office.getOfficeID() == id)
                .findFirst().orElse(null);
        if (ticketOffice == null) {
            return  -1;
        }

        ticketOffice.closeOffice();
        System.out.println("Hall: Ticket office " + ticketOffice.getOfficeID() + " closed.");
        transferClients(ticketOffice);

        return id;
    }

    public int openTicketOffice(int id) {
        TicketOffice ticketOffice = ticketOffices
                .stream().filter(office -> office.getOfficeID() == id)
                .findFirst().orElse(null);
        if (ticketOffice == null) {
            return  -1;
        }

        ticketOffice.openOffice();
        System.out.println("Hall: Ticket office " + ticketOffice.getOfficeID() + " open.");

        return id;
    }

    private void transferClients(TicketOffice ticketOffice) {
        if (reservedTicketOffice == null) {
            throw new IllegalStateException("Reserved ticket office is not set");
        }

        if (ticketOffice.getQueueSize() == 0)
        {
            System.out.println("Hall: nothing to transfer from ticket office " + ticketOffice.getOfficeID());
            return;
        }

        reservedTicketOffice.setQueue(ticketOffice.getQueue());
        ticketOffice.clearQueue();

        QueueUpdate queueUpdate = new QueueUpdate(ticketOffice.getOfficeID(), ticketOffice
                .getQueue()
                .stream()
                .map(Client::getClientID)
                .mapToInt(i -> i).toArray());

        QueueTransferedEvent event = new QueueTransferedEvent(this, queueUpdate);
        applicationEventPublisher.publishEvent(event);
        System.out.println("Hall: Clients transferred from ticket office " + ticketOffice.getOfficeID() + " to reserved ticket office.");
    }

    // Selects a random entrance
    private Entrance selectRandomEntrance() {
        int index = random.nextInt(entrances.size());
        return entrances.get(index);
    }

    // Publishes a ClientCreatedEvent
    private void publishClientCreatedEvent(Client client, Entrance entrance, TicketOffice ticketOffice, double time) {
        ClientCreatedEvent event = new ClientCreatedEvent(
                this,
                new ClientCreated(client, entrance.getId(), ticketOffice.getOfficeID(), time)
        );
        applicationEventPublisher.publishEvent(event);
    }

    // Handles thread sleep with exception handling
    private void imitateMoving(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.err.println("Thread interrupted: " + e.getMessage());
        }
    }

    public Segment getSegment() {
        return segment;
    }
}
