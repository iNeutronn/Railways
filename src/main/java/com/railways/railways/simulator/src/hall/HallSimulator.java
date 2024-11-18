package com.railways.railways.simulator.src.hall;

public class HallSimulator {
    private final TicketOfficeSimulator ticketOfficeSimulator;
    private final IGeneration clientGenerator;

    private final int maxCapacity; // Maximum capacity of the hall
    private int currentClientCount = 0; // Current number of clients in the hall

    public HallSimulator(TicketOfficeSimulator ticketOfficeSimulator, IGeneration clientGenerator, int maxCapacity) {
        this.ticketOfficeSimulator = ticketOfficeSimulator;
        this.clientGenerator = clientGenerator;
        this.maxCapacity = maxCapacity;
    }

    public void run() {
        clientGenerator.Resume(); // Start generating clients

        while (true) { // Continuous simulation
            if (currentClientCount < maxCapacity) {
                Client client = clientGenerator.Generate();
                if (client != null) {
                    currentClientCount++;
                    // client entered the hall
                    //ticketOfficeSimulator.processClient(client);
                }
            }
            else {
                clientGenerator.Stop();
            }

            // Resume generation if the count drops below 70% of the norm
            if (currentClientCount < 0.7 * maxCapacity && clientGenerator instanceof ClientGenerator && ((ClientGenerator) clientGenerator).isStopped) {
                clientGenerator.Resume();
            }

            try {
                Thread.sleep(1000); // Simulate time delay (1 second per cycle)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

