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

public class TicketOffice implements Runnable {
    private final int ticketOfficeID;
    private final PriorityBlockingQueue<Client> clientsQueue;
    private final ArrayList<ServeRecord> serveRecords;
    private boolean isOpen;
    private final Object pauseLock = new Object(); // Lock object for synchronization
    private Segment segment;
    private Direction direction;
    private ApplicationEventPublisher eventPublisher;
    private final DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    private final int minServiceTime;
    private final int maxServiceTime;

    private final Logger logger;

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

    public void serveClient() throws InterruptedException {
        Client client = clientsQueue.take();

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

    private int getRandomServeTime() {
        Random random = new Random();
        return random.nextInt(maxServiceTime - minServiceTime + 1) + minServiceTime;
    }

    public void addClient(Client client) {
        clientsQueue.offer(client); // Thread-safe addition
        logger.log("Client " + client.getFullName() +
                " (ID: " + client.getClientID() + ") added to the queue.", LogLevel.Info);
        publishQueueUpdate();
    }

    public void closeOffice() {
        synchronized (pauseLock) {
            isOpen = false; // Mark as closed
        }
        logger.log("TicketOffice " + ticketOfficeID + " closed.", LogLevel.Info);
    }

    public void openOffice() {
        synchronized (pauseLock) {
            isOpen = true; // Mark as open
            pauseLock.notifyAll(); // Notify all waiting threads
        }
        logger.log("TicketOffice " + ticketOfficeID + " opened.", LogLevel.Info);
    }

    public ArrayList<ServeRecord> getServeRecords() {
        return serveRecords;
    }

    public int getQueueSize() {
        return clientsQueue.size();
    }

    public Segment getSegment() {
        return segment;
    }

    public Direction getDirection() {
        return direction;
    }

    public PriorityBlockingQueue<Client> getQueue() {
        return clientsQueue;
    }

    public int getOfficeID() {
        return ticketOfficeID;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

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

    public void setQueue(PriorityBlockingQueue<Client> queue) {
        clientsQueue.addAll(queue);
    }

    public void clearQueue() {
        clientsQueue.clear();
    }
}
