package com.railways.railways.simulator.src.hall;

public class HallSimulator {
    private TicketOfficeSimulator ticketOfficeSimulator;
    private IGeneration clientGenerator;

    public HallSimulator(TicketOfficeSimulator ticketOfficeSimulator, IGeneration clientGenerator) {
        this.ticketOfficeSimulator = ticketOfficeSimulator;
        this.clientGenerator = clientGenerator;
    }

    public void run() {
        ticketOfficeSimulator.run();
        clientGenerator.Generate();
    }
}
