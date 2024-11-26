package com.railways.railways.domain.station;
import com.railways.railways.Configuration.ConfigModel;
import com.railways.railways.Configuration.MapSize;
import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.client.ClientCreated;
import com.railways.railways.events.ClientCreatedEvent;
import com.railways.railways.events.QueueTransferedEvent;
import com.railways.railways.logging.Logger;
import com.railways.railways.logging.LogLevel;
import org.springframework.context.ApplicationEventPublisher;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Represents a Hall in a railway system containing ticket offices and entrances.
 * It handles client processing, ticket office management, and client movement simulation.
 */
public class Hall {

    private List<TicketOffice> ticketOffices;
    private List<Entrance> entrances;
    private TicketOffice reservedTicketOffice;
    private MapSize mapSize;
    private ApplicationEventPublisher applicationEventPublisher;
    private final Random random;
    private double moveSpeed = 100.0;
    private final Logger logger;

    /**
     * Sets the ApplicationEventPublisher for event publishing.
     *
     * @param applicationEventPublisher the event publisher to be set
     */
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Constructs a Hall with the specified map size and logger.
     *
     * @param mapSize the size of the map
     * @param logger the logger used for logging events
     */
    public Hall(MapSize mapSize, Logger logger) {
        this.logger = logger;

        this.mapSize = mapSize;
        this.random = new Random();
        this.ticketOffices = new ArrayList<>();
        this.entrances = new ArrayList<>();
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

    /**
     * Processes a client by selecting an entrance, finding the best ticket office, and simulating movement.
     *
     * @param client the client to process
     */
    public void processClient(Client client) {
        logger.log("Processing client: " + client.getFullName(), LogLevel.Info);

        Entrance selectedEntrance = selectRandomEntrance();
        logger.log("Selected entrance ID: " + selectedEntrance.getId(), LogLevel.Debug);

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
        logger.log("Client " + client.getFullName() +
                " added to ticket office " + ticketOffice.getOfficeID()
                + " with move time: " + distance * moveSpeed, LogLevel.Info);
    }

    /**
     * Gets the total number of clients across all ticket offices.
     *
     * @return the total client count
     */
    public  int getClientCount() {
        int count = 0;
        for (TicketOffice ticketOffice : ticketOffices) {
            count += ticketOffice.getQueueSize();
        }
        return count;
    }

    /**
     * Finds the best ticket office for a client based on distance and queue size.
     *
     * @param entrancePoint the client's current location (entrance point)
     * @return the best ticket office
     */
    public TicketOffice getBestTicketOffice(Point entrancePoint) {
        logger.log("Finding best ticket office for entrance point: " + entrancePoint, LogLevel.Info);
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
        logger.log("Best office selected: " + (bestOffice != null ? bestOffice.getOfficeID() : "None"), LogLevel.Info);
        return bestOffice;
    }

    /**
     * Closes a ticket office by the given ID and transfers its clients to the reserved ticket office.
     *
     * @param id the ID of the ticket office to close
     * @return the ID of the closed office or -1 if not found
     */
    public int closeTicketOffice(int id) {
        TicketOffice ticketOffice = ticketOffices
                .stream().filter(office -> office.getOfficeID() == id)
                .findFirst().orElse(null);
        if (ticketOffice == null) {
            logger.log("Attempt to close non-existent ticket office with ID: " + id, LogLevel.Error);
            return  -1;
        }

        ticketOffice.closeOffice();
        logger.log("Ticket office " + ticketOffice.getOfficeID() + " closed.", LogLevel.Warning);
        transferClients(ticketOffice);

        return id;
    }

    /**
     * Opens a ticket office by the given ID.
     *
     * @param id the ID of the ticket office to open
     * @return the ID of the opened office or -1 if not found
     */
    public int openTicketOffice(int id) {
        TicketOffice ticketOffice = ticketOffices
                .stream().filter(office -> office.getOfficeID() == id)
                .findFirst().orElse(null);
        if (ticketOffice == null) {
            logger.log("Attempt to open non-existent ticket office with ID: " + id, LogLevel.Error);
            return  -1;
        }

        ticketOffice.openOffice();
        logger.log("Hall: Ticket office " + ticketOffice.getOfficeID() + " open.", LogLevel.Info);

        return id;
    }

    /**
     * Transfers clients from a closed ticket office to the reserved ticket office.
     *
     * @param ticketOffice the ticket office whose clients will be transferred
     */
    private void transferClients(TicketOffice ticketOffice) {
        if (reservedTicketOffice == null) {
            logger.log("Reserved ticket office is not set", LogLevel.Error);
            throw new IllegalStateException("Reserved ticket office is not set");
        }

        if (ticketOffice.getQueueSize() == 0)
        {
            logger.log("Hall: nothing to transfer from ticket office " + ticketOffice.getOfficeID(),
                    LogLevel.Warning);
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
        logger.log("Hall: Clients transferred from ticket office " + ticketOffice.getOfficeID() + " to reserved ticket office.", LogLevel.Info);
    }

    /**
     * Selects a random entrance from the list of entrances.
     *
     * @return a randomly selected entrance
     */
    private Entrance selectRandomEntrance() {
        int index = random.nextInt(entrances.size() - 1);
        return entrances.get(index);
    }

    /**
     * Publishes a ClientCreatedEvent when a client is processed.
     *
     * @param client the client being processed
     * @param entrance the entrance the client is using
     * @param ticketOffice the ticket office the client is directed to
     * @param time the time taken for the client to reach the office
     */
    private void publishClientCreatedEvent(Client client, Entrance entrance, TicketOffice ticketOffice, double time) {
        ClientCreatedEvent event = new ClientCreatedEvent(
                this,
                new ClientCreated(client, entrance.getId(), ticketOffice.getOfficeID(), time)
        );
        applicationEventPublisher.publishEvent(event);
    }

    /**
     * Imitates the client's movement by sleeping the current thread for the given amount of time.
     *
     * @param millis the time in milliseconds to simulate movement
     */
    private void imitateMoving(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            logger.log("Thread interrupted while imitating moving: " + e.getMessage(), LogLevel.Error);
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }
}
