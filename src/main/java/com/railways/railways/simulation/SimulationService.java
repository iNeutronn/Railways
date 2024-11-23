package com.railways.railways.simulation;

import com.railways.railways.domain.station.Direction;
import com.railways.railways.domain.station.Hall;
import com.railways.railways.domain.station.Segment;
import com.railways.railways.domain.station.TicketOffice;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SimulationService {

    private final Hall hall;
    private final ExecutorService executorService;
    private  final ApplicationEventPublisher eventPublisher;

    public SimulationService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        this.hall = Hall.getInstance(); // Singleton instance
        this.executorService = Executors.newCachedThreadPool(); // Flexible thread pool
        setupHall();
    }

    private void setupHall() {
        // Create ticket offices
        List<TicketOffice> ticketOffices = new ArrayList<>();
        TicketOffice ticketOffice1 = new TicketOffice(
                1,
                new Segment(new Point(0, 0), new Point(2, 2)),
                Direction.Up,
                500,
                1000
        );
        TicketOffice ticketOffice2 = new TicketOffice(
                2,
                new Segment(new Point(10, 10), new Point(12, 12)),
                Direction.Up,
                500,
                1000
        );

        ticketOffices.add(ticketOffice1);
        ticketOffices.add(ticketOffice2);

        // Set ticket offices to the hall
        hall.setTicketOffices(ticketOffices);

        // Configure entrances
        List<Segment> entrances = new ArrayList<>();
        entrances.add(new Segment(new Point(0, 0), new Point(0, 1)));
        entrances.add(new Segment(new Point(9, 9), new Point(10, 9)));

        hall.setEntrances(entrances);
    }

    public void startSimulation() {
        // Start ticket office threads
        for (TicketOffice office : hall.getTicketOffices()) {
            executorService.submit(office);
        }

        // Start hall simulation
        HallSimulator hallSimulator = new HallSimulator(eventPublisher, hall, new IntervalPolicy(0.5), 10, true);
        Thread simulationThread = new Thread(hallSimulator::run);
        simulationThread.start();

        System.out.println("Simulation started!");
    }
}
