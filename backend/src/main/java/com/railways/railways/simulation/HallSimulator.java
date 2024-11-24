package com.railways.railways.simulation;

import com.railways.railways.Configuration.ConfigModel;
import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.client.ClientGenerator;
import com.railways.railways.domain.station.Hall;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public HallSimulator(ApplicationEventPublisher eventPublisher, ConfigModel appConfig, Hall hall, ClientGenerator clientGenerator) {
        this.hall = hall;
        this.appConfig = appConfig;
        this.clientGenerator = clientGenerator;
        this.executorService = Executors.newCachedThreadPool();
        this.eventPublisher = eventPublisher;
    }

    public void start() {
        synchronized (pauseLock) {
            isRunning = true;
            pauseLock.notifyAll(); // Wake up the thread if it is waiting
        }
    }

    public void stop() {
        synchronized (pauseLock) {
            isRunning = false; // The thread will pause the next time it checks
        }
    }

    @Override
    public void run() {
        while (true) {
            synchronized (pauseLock) {
                while (!isRunning) { // If paused, wait until notified
                    try {
                        pauseLock.wait(); // Release lock and wait to be resumed
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Thread interrupted, exiting simulation");
                        return;
                    }
                }
            }

            if (hall.getClientCount() < appConfig.getMaxPeopleAllowed()) {
                Client client = clientGenerator.generateClient(random.nextInt(100000));
                if (client != null) {
                    executorService.submit(() -> hall.processClient(client));
                    System.out.println("HallSimulator: Client created and added");
                }
            } else {
                System.out.println("Hall is full, pausing simulation");
                isOvercrowded = true;
            }

            sleep((int) (appConfig.getGenerationPolicy().getSeconds() * 1000));

            if (isOvercrowded)
            {
                System.out.println("Hall is overcrowded, checking condition to resume simulation");
                if (hall.getClientCount() < appConfig.getMaxPeopleAllowed() * 0.7) {
                    isOvercrowded = false;
                    System.out.println("Hall is no longer overcrowded, resuming simulation");
                } else {
                    sleep(5000);
                }
            }
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}


