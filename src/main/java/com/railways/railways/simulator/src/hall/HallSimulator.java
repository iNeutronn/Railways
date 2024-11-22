package com.railways.railways.simulator.src.hall;

import java.awt.*;

public class HallSimulator {
    private final TicketOfficeSimulator ticketOfficeSimulator;

    private final int maxCapacity; // Maximum capacity of the hall
    private GenerationPolicy schedulingPolicy;
    private int currentClientCount = 0; // Current number of clients in the hall
    private boolean isRunning = false;

    public HallSimulator(TicketOfficeSimulator ticketOfficeSimulator, GenerationPolicy policy, int maxCapacity, boolean isRunning) {
        this.ticketOfficeSimulator = ticketOfficeSimulator;
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
            while (isRunning) { // Continuous simulation
                if (currentClientCount < maxCapacity) {
                    Client client = generateClient();
                    if (client != null) {
                        currentClientCount++;
                        // client entered the hall
                        //ticketOfficeSimulator.processClient(client);
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

