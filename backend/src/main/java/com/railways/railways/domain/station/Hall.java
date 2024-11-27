package com.railways.railways.domain.station;
import com.railways.railways.AppConfig;
import com.railways.railways.Configuration.ConfigModel;
import com.railways.railways.Configuration.EntranceConfig;
import com.railways.railways.Configuration.MapSize;
import com.railways.railways.Configuration.WebSocketConfig;
import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.client.ClientCreated;
import com.railways.railways.domain.client.ClientRedirected;
import com.railways.railways.events.ClientCreatedEvent;
import com.railways.railways.events.ClientRedirectedEvent;
import com.railways.railways.events.QueueTransferedEvent;
import com.railways.railways.logging.Logger;
import com.railways.railways.logging.LogLevel;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Represents a Hall in a railway system containing ticket offices and entrances.
 * It handles client processing, ticket office management, and client movement simulation.
 */
public class Hall {

    private final List<Client> clientsThatAreGoingToCashPoint = new ArrayList<>();
    private List<TicketOffice> ticketOffices;
    private TicketOffice reservedTicketOffice;
    private ApplicationEventPublisher applicationEventPublisher;
    private final Random random;
    private double moveSpeed = 100.0;
    private final Logger logger;
    private final ConfigModel config;

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
     * @param logger the logger used for logging events
     */
    public Hall(ConfigModel config, Logger logger) {
        this.logger = logger;
        this.config = config;
        this.random = new Random();
        this.ticketOffices = new ArrayList<>();

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

        EntranceConfig selectedEntrance = selectRandomEntrance();
        logger.log("Selected entrance ID: " + selectedEntrance.id, LogLevel.Debug);


        var entrancePoint = new Point(selectedEntrance.x, selectedEntrance.y);

        var ticketOffice = getBestTicketOffice(entrancePoint);

        clientsThatAreGoingToCashPoint.add(client);
        publishClientCreatedEvent(client, selectedEntrance, ticketOffice);

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
        return count + clientsThatAreGoingToCashPoint.size();
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
    private EntranceConfig selectRandomEntrance() {
        int index = random.nextInt(0, config.getEntranceCount() );
        return config.getEntranceConfigs().get(index);
    }

    /**
     * Publishes a ClientCreatedEvent when a client is processed.
     *
     * @param client the client being processed
     * @param entrance the entrance the client is using
     * @param ticketOffice the ticket office the client is directed to
     */
    private void publishClientCreatedEvent(Client client, EntranceConfig entrance, TicketOffice ticketOffice) {
        ClientCreatedEvent event = new ClientCreatedEvent(
                this,
                new ClientCreated(client, entrance.id, ticketOffice.getOfficeID())
        );
        applicationEventPublisher.publishEvent(event);
    }

    private void waitToReachCashPoint(@NonNull Client client) {
      while (true)
        {
            if(!client.isClientGoingToCashPoint().get())
                break;

        }
    }

    public void clientReachedCashPoint(int clientId,int cashPointId) {
        Client client = clientsThatAreGoingToCashPoint.stream().filter(c -> c.getClientID() == clientId).findFirst().orElse(null);

        TicketOffice ticketOffice = ticketOffices.stream().
                filter(t -> t.getOfficeID() == cashPointId).
                findFirst().
                orElse(null);

        if (ticketOffice == null) {
            logger.log("Ticket office with ID " + cashPointId + " not found", LogLevel.Error);
            return;
        }

        if (ticketOffice.isOpen())
        {
            ticketOffice.addClient(client);
            clientsThatAreGoingToCashPoint.remove(client);
            return;
        }
        TicketOffice newTicketOffice = getBestTicketOffice(ticketOffice.getSegment().getStart());
//        newTicketOffice.addClient(client);

        ClientRedirected redirected = new ClientRedirected(clientId, newTicketOffice.getOfficeID());
        ClientRedirectedEvent event = new ClientRedirectedEvent(this, redirected);
        applicationEventPublisher.publishEvent(event);

    }
}
