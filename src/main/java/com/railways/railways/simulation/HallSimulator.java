package com.railways.railways.simulation;

import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.station.Hall;

import java.awt.*;

public class HallSimulator {
    private final int maxCapacity; // Maximum capacity of the hall
    private GenerationPolicy schedulingPolicy;
    private Hall hall;
    private boolean isRunning = false;

    public HallSimulator(Hall hall, GenerationPolicy policy, int maxCapacity, boolean isRunning) {
        this.hall = hall;
        this.schedulingPolicy = policy;
        this.maxCapacity = maxCapacity;
        this.isRunning = isRunning;
    }

    private Client generateClient() {
        return new Client(
                1,
                "First name", // change to random
                "Last name", // change to random
                3, // change to random
                new Point() // change to random
        );
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
                    Client client = generateClient();
                    if (client != null) {
                        hall.addClient(client);
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

            if(currentClientCount < maxCapacity) {
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

