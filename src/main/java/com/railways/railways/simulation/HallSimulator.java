package com.railways.railways.simulation;

import com.railways.railways.AppConfig;
import com.railways.railways.Configuration.ConfigModel;
import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.client.ClientGenerator;
import com.railways.railways.domain.station.Hall;
import com.railways.railways.events.ClientCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HallSimulator {
    private Hall hall;
    private boolean isRunning = false;
    private Random random = new Random();
    private ApplicationEventPublisher applicationEventPublisher;
    private ClientGenerator clientGenerator;
    private ConfigModel appConfig;
    private ExecutorService executorService;

    public HallSimulator(ApplicationEventPublisher applicationEventPublisher, ConfigModel appConfig, Hall hall, ClientGenerator clientGenerator) {
        this.hall = hall;
        this.applicationEventPublisher = applicationEventPublisher;
        this.appConfig = appConfig;
        this.clientGenerator = clientGenerator;
        this.executorService = Executors.newCachedThreadPool();
    }

    public void start() {
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    public void run() {

        while (true)
        {
            int currentClientCount = hall.getClientCount();
            while (isRunning) { // Continuous simulation
                currentClientCount = hall.getClientCount();
                if (currentClientCount < appConfig.getMaxPeopleAllowed()) {
                    Client client = clientGenerator.generateClient(random.nextInt(100000));

                    ClientCreatedEvent event = new ClientCreatedEvent(this, client);
                    applicationEventPublisher.publishEvent(event);

                    if (client != null) {
                        executorService.submit(() -> hall.addClient(client));
//                        hall.addClient(client);
                        System.out.println("HallSimulator: Client created and added");
                    }
                } else {
                    stop();
                }

                try {
                    Thread.sleep((long) (appConfig.getGenerationPolicy().getSeconds() * 1000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            if(currentClientCount < appConfig.getMaxPeopleAllowed() * 0.75) {
                start();
            }else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}

