package com.railways.railways.simulation;

import com.railways.railways.Configuration.ConfigModel;
import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.client.ClientGenerator;
import com.railways.railways.domain.station.Hall;
import com.railways.railways.logging.LogLevel;
import com.railways.railways.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * HallSimulator is responsible for simulating the operation of a hall within the railway system.
 * It manages the creation and processing of clients in the hall, considering the maximum capacity
 * and using the configured generation policy for client arrivals.
 * This simulator runs in a separate thread, creating clients at specified intervals and handling
 * overcrowding conditions by pausing or resuming the simulation based on the current hall capacity.
 */
public class HallSimulator implements Runnable {
    private final Hall hall;
    private final Random random = new Random();
    private final ClientGenerator clientGenerator;

    private final ConfigModel appConfig;
    private final ExecutorService executorService;
    private final ApplicationEventPublisher eventPublisher;
    private boolean isOvercrowded = false;

    // Shared flag and monitor
    private boolean isRunning = false;
    private final Object pauseLock = new Object();

    private final Logger logger;

    /**
     * Constructor initializes the HallSimulator with required dependencies.
     *
     * @param eventPublisher Spring event publisher to publish events.
     * @param appConfig Configuration model containing simulation settings.
     * @param hall Hall object that holds and manages clients.
     * @param clientGenerator Generates new clients to be added to the hall.
     * @param logger Logger for recording simulation events and status.
     */
    public HallSimulator(ApplicationEventPublisher eventPublisher, ConfigModel appConfig, Hall hall, ClientGenerator clientGenerator, Logger logger) {
        this.hall = hall;
        this.appConfig = appConfig;
        this.clientGenerator = clientGenerator;
        this.executorService = Executors.newCachedThreadPool();
        this.eventPublisher = eventPublisher;
        this.logger = logger;

        logger.log("HallSimulator initialized with " + appConfig.getMaxPeopleAllowed()
                + " people and generation policy: " + appConfig.getGenerationPolicy().getSeconds() + " seconds", LogLevel.Info);
    }

    /**
     * Starts the simulation, allowing clients to be created and processed.
     * The simulation will continue running until paused or stopped.
     */
    public void start() {
        synchronized (pauseLock) {
            isRunning = true;
            logger.log("Simulation started", LogLevel.Info);
            pauseLock.notifyAll(); // Wake up the thread if it is waiting
        }
    }

    /**
     * Pauses the simulation, halting further client creation and processing until resumed.
     */
    public void stop() {
        synchronized (pauseLock) {
            isRunning = false; // The thread will pause the next time it checks
            logger.log("Simulation paused", LogLevel.Warning);
        }
    }

    /**
     * The main method that runs the hall simulation in a separate thread.
     * It continuously generates clients and processes them, while checking if the hall is overcrowded.
     * The simulation pauses when the hall is full and resumes once the number of clients drops below a threshold.
     */
    @Override
    public void run() {
        logger.log("HallSimulator started running.", LogLevel.Info);
        while (true) {
            synchronized (pauseLock) {
                while (!isRunning) { // If paused, wait until notified
                    try {
                        pauseLock.wait(); // Release lock and wait to be resumed
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        logger.log("Thread interrupted, exiting simulation", LogLevel.Warning);
                        return;
                    }
                }
            }

            if (hall.getClientCount() < appConfig.getMaxPeopleAllowed()) {
                Client client = clientGenerator.generateClient(random.nextInt(100000));
                if (client != null) {
                    executorService.submit(() -> hall.processClient(client));
                    logger.log("HallSimulator: Client created and added", LogLevel.Info);
                }
            } else {
                logger.log("Hall is full, pausing simulation", LogLevel.Warning);
                isOvercrowded = true;
            }

            sleep((int) (appConfig.getGenerationPolicy().getSeconds() * 1000));

            if (isOvercrowded)
            {
                logger.log("Hall is overcrowded, checking condition to resume simulation", LogLevel.Warning);
                if (hall.getClientCount() < appConfig.getMaxPeopleAllowed() * 0.7) {
                    isOvercrowded = false;
                    logger.log("Hall is no longer overcrowded, resuming simulation", LogLevel.Warning);
                } else {
                    sleep(5000);
                }
            }
        }
    }

    /**
     * Helper method to sleep the thread for a specified number of milliseconds.
     *
     * @param millis Time in milliseconds to sleep.
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}


