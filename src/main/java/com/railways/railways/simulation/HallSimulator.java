package com.railways.railways.simulation;

import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.client.ClientGenerator;
import com.railways.railways.domain.station.Hall;
import com.railways.railways.events.ClientCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import java.util.Random;

public class HallSimulator {
    private final int maxCapacity; // Maximum capacity of the hall
    private GenerationPolicy schedulingPolicy;
    private Hall hall;
    private boolean isRunning = false;
    private Random random = new Random();
    private ApplicationEventPublisher applicationEventPublisher;
    private ClientGenerator clientGenerator;

    public HallSimulator(ApplicationEventPublisher applicationEventPublisher, Hall hall, GenerationPolicy policy, int maxCapacity, ClientGenerator clientGenerator) {
        this.hall = hall;
        this.applicationEventPublisher = applicationEventPublisher;
        this.schedulingPolicy = policy;
        this.maxCapacity = maxCapacity;

        this.clientGenerator = clientGenerator;
    }

    public void setPolicy(GenerationPolicy schedulingPolicy)
    {
        this.schedulingPolicy = schedulingPolicy;
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
                if (currentClientCount < maxCapacity) {
                    Client client = clientGenerator.generateClient(random.nextInt(100000));

                    ClientCreatedEvent event = new ClientCreatedEvent(this, client);
                    applicationEventPublisher.publishEvent(event);

                    if (client != null) {
                        hall.addClient(client);
                        System.out.println("HallSimulator: Client created and added");
                    }
                } else {
                    stop();
                }

                try {
                    Thread.sleep((long) (schedulingPolicy.getSeconds() * 1000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            if(currentClientCount < maxCapacity * 0.75) {
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

