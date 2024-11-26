package com.railways.railways.domain.station;

import com.railways.railways.domain.ServeRecord;
import com.railways.railways.domain.client.Client;
import com.railways.railways.events.ClientServedEvent;
import com.railways.railways.events.QueueUpdatedEvent;
import com.railways.railways.logging.LogLevel;
import com.railways.railways.logging.Logger;
import org.springframework.context.ApplicationEventPublisher;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Represents a Ticket Office in the railway system.
 * The office serves clients, maintains a queue, and tracks service records.
 * This class implements Runnable to handle the client-serving process in a separate thread.
 */
public class TicketOffice implements Runnable {
    /** The ID of the ticket office. */
    private final int ticketOfficeID;
    /** The queue of clients waiting to be served. */
    private final PriorityBlockingQueue<Client> clientsQueue;
    /** The list of records for served clients. */
    private final ArrayList<ServeRecord> serveRecords;
    /** Flag indicating whether the office is open or closed. */
    private boolean isOpen;
    /** Lock object used for synchronization during open/close operations. */
    private final Object pauseLock = new Object();
    /** The segment where the ticket office is located. */
    private Segment segment;
    /** The direction the ticket office serves. */
    private Direction direction;
    /** The event publisher used to publish events such as client served or queue updated. */
    private ApplicationEventPublisher eventPublisher;
    /** Formatter for the time. */
    private final DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
    /** The minimum service time for a client. */
    private final int minServiceTime;
    /** The maximum service time for a client. */
    private final int maxServiceTime;
    /** The logger used for logging events and actions. */
    private final Logger logger;

    private final Object queueLock = new Object();


    /**
     * Constructs a TicketOffice with specified properties.
     *
     * @param eventPublisher the event publisher used for publishing events
     * @param ticketOfficeID the unique ID for this ticket office
     * @param position the segment where the office is located
     * @param direction the direction the ticket office serves
     * @param minServiceTime the minimum time to serve a client
     * @param maxServiceTime the maximum time to serve a client
     * @param logger the logger used to log actions
     */
    public TicketOffice(ApplicationEventPublisher eventPublisher, int ticketOfficeID, Segment position, Direction direction, int minServiceTime, int maxServiceTime, Logger logger) {
        this.eventPublisher = eventPublisher;
        this.ticketOfficeID = ticketOfficeID;
        this.direction = direction;
        this.segment = position;

        this.clientsQueue = new PriorityBlockingQueue<>(
                10,
                (c1, c2) -> {
                    int priorityComparison = Integer.compare(c2.getPriority(), c1.getPriority());
                    return priorityComparison != 0 ? priorityComparison : Long.compare(c1.getTimestamp(), c2.getTimestamp());
                }
        );

        this.serveRecords = new ArrayList<>();
        this.isOpen = true;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;

        this.logger = logger;

        logger.log("TicketOffice created with ID: " + ticketOfficeID +
                ", Direction: " + direction + ", Segment: " + segment, LogLevel.Info);
    }


    /**
     * The main logic for serving clients is executed in this method. It runs in a loop, serving clients in the queue.
     */
    @Override
    public void run() {
        while (true) {
            synchronized (pauseLock) {
                while (!isOpen) {
                    try {
                        pauseLock.wait(); // Wait until notified to resume
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        logger.log("TicketOffice thread interrupted", LogLevel.Warning);
                        return; // Exit the thread gracefully
                    }
                }
            }

            sleep(100); // Short pause between serving clients
            try {
                serveClient();
            } catch (InterruptedException e) {
                logger.log("Error while serving client: " + e.getMessage(), LogLevel.Error);
                throw new RuntimeException(e);
            }

        }
    }

    /**
     * Serves a client from the queue. The client is processed and a service record is created.
     *
     * @throws InterruptedException if the thread is interrupted while serving
     */
    public void serveClient() throws InterruptedException {
        Client client;
        while (true)
        {
            synchronized (queueLock) {
                if(!clientsQueue.isEmpty()) {
                    client = clientsQueue.take();
                    break;
                }
            }
        }
        int serviceTime = getRandomServeTime();
        String startTime = ZonedDateTime.now().format(isoFormatter);

        logger.log("Serving client " + client.getFullName() +
                " (ID: " + client.getClientID() + ") at ticket office " + ticketOfficeID, LogLevel.Debug);

        sleep((long) serviceTime * client.getTicketsToBuy());

        String endTime = ZonedDateTime.now().format(isoFormatter);

        ServeRecord record = new ServeRecord(ticketOfficeID, client.getClientID(), client.getTicketsToBuy(), startTime, endTime);
        serveRecords.add(record);
        logger.log("TicketOffice: Client " + client.getFullName() + " with id:" +
                client.getClientID() + " served by ticket office " + ticketOfficeID, LogLevel.Info);

        ClientServedEvent event = new ClientServedEvent(this, record);
        eventPublisher.publishEvent(event);
    }

    /**
     * Generates a random service time within the defined range (min and max).
     *
     * @return the random service time in milliseconds
     */
    private int getRandomServeTime() {
        Random random = new Random();
        return random.nextInt(maxServiceTime - minServiceTime + 1) + minServiceTime;
    }

    /**
     * Adds a client to the queue for serving.
     *
     * @param client the client to be added
     */
    public void addClient(Client client) {
        synchronized (queueLock) {
            clientsQueue.offer(client); // Thread-safe addition
            publishQueueUpdate();
        }
        logger.log("Client " + client.getFullName() +
                " (ID: " + client.getClientID() + ") added to the queue.", LogLevel.Info);
    }

    /**
     * Closes the ticket office, preventing new clients from being served.
     */
    public void closeOffice() {
        synchronized (pauseLock) {
            isOpen = false; // Mark as closed
        }
        logger.log("TicketOffice " + ticketOfficeID + " closed.", LogLevel.Info);
    }

    /**
     * Opens the ticket office, allowing it to serve clients again.
     */
    public void openOffice() {
        synchronized (pauseLock) {
            isOpen = true; // Mark as open
            pauseLock.notifyAll(); // Notify all waiting threads
        }
        logger.log("TicketOffice " + ticketOfficeID + " opened.", LogLevel.Info);
    }

    /**
     * Gets the list of records for clients who have been served by this office.
     *
     * @return the list of service records
     */
    public ArrayList<ServeRecord> getServeRecords() {
        return serveRecords;
    }

    /**
     * Returns the current size of the queue.
     *
     * @return the number of clients in the queue
     */
    public int getQueueSize() {
        return clientsQueue.size();
    }

    /**
     * Gets the segment where the ticket office is located.
     *
     * @return the segment of the ticket office
     */
    public Segment getSegment() {
        return segment;
    }

    /**
     * Gets the direction the ticket office serves.
     *
     * @return the direction of the ticket office
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Gets the queue of clients waiting to be served.
     *
     * @return the queue of clients
     */
    public PriorityBlockingQueue<Client> getQueue() {
        return clientsQueue;
    }

    /**
     * Gets the ID of the ticket office.
     *
     * @return the ticket office ID
     */
    public int getOfficeID() {
        return ticketOfficeID;
    }

    /**
     * Sleeps the thread for the specified time.
     *
     * @param millis the time to sleep in milliseconds
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Checks if the ticket office is open.
     *
     * @return true if the ticket office is open, false otherwise
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Publishes an event when the queue is updated.
     */
    public void publishQueueUpdate() {
        QueueUpdate queueUpdate = new QueueUpdate(ticketOfficeID, clientsQueue
                .stream()
                .map(Client::getClientID)
                .mapToInt(i -> i)
                .toArray());
        QueueUpdatedEvent event = new QueueUpdatedEvent(this, queueUpdate);

        eventPublisher.publishEvent(event);
        logger.log("Queue updated for TicketOffice " + ticketOfficeID, LogLevel.Debug);
    }

    /**
     * Sets a new queue for the ticket office.
     *
     * @param queue the new queue to set
     */
    public void setQueue(PriorityBlockingQueue<Client> queue) {
        clientsQueue.addAll(queue);
    }

    /**
     * Clears the queue of all clients.
     */
    public void clearQueue() {
        clientsQueue.clear();
    }
}
