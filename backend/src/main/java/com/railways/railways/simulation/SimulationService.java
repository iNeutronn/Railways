package com.railways.railways.simulation;

import com.railways.railways.Configuration.CashPointConfig;
import com.railways.railways.Configuration.ConfigModel;
import com.railways.railways.Configuration.EntranceConfig;
import com.railways.railways.domain.client.ClientGenerator;
import com.railways.railways.domain.client.PrivilegeEnum;
import com.railways.railways.domain.station.Entrance;
import com.railways.railways.domain.station.Hall;
import com.railways.railways.domain.station.Segment;
import com.railways.railways.domain.station.TicketOffice;
import com.railways.railways.logging.LogLevel;
import com.railways.railways.logging.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The SimulationService class is responsible for managing the simulation of a railway station hall.
 * It configures the hall, ticket offices, entrances, and manages the client generation and service.
 * It provides methods for starting, stopping, and resuming the simulation as well as controlling cash points.
 */
@Service
public class SimulationService {
    private Hall hall;
    private ExecutorService executorService;
    private final ClientGenerator clientGenerator;
    private HallSimulator hallSimulator;
    private final ApplicationEventPublisher eventPublisher;
    private final ConfigModel appConfig;

    private final Logger logger;

    /**
     * Constructor for initializing the SimulationService with necessary configurations.
     *
     * @param eventPublisher The publisher used to publish application events.
     * @param appConfig Configuration model containing the simulation settings.
     * @param logger Logger used to log simulation activities.
     */
    public SimulationService(ApplicationEventPublisher eventPublisher, ConfigModel appConfig, Logger logger) {
        this.eventPublisher = eventPublisher;
        this.appConfig = appConfig;
        this.hall = new Hall(appConfig, logger);
        this.executorService = Executors.newCachedThreadPool(); // Flexible thread pool
        Map<PrivilegeEnum, Integer> privilegeMap = Map.of(
                PrivilegeEnum.DEFAULT, 70,
                PrivilegeEnum.WARVETERAN, 10,
                PrivilegeEnum.WITHCHILD, 10,
                PrivilegeEnum.DISABLED, 10
        );
        this.logger = logger;

        this.clientGenerator = new ClientGenerator(privilegeMap, logger);

        setupHall();
    }

    /**
     * Configures the hall by setting up ticket offices, entrances, and other necessary components.
     */
    private void setupHall() {
        hall.setApplicationEventPublisher(eventPublisher);

        // Create ticket offices
        List<TicketOffice> ticketOffices = new ArrayList<>();
        for (int i = 0; i < appConfig.getCashPointCount(); i++) {
             CashPointConfig cashPointConfig = appConfig.getCashpointConfigs().get(i);

            Segment segment = new Segment(new Point(cashPointConfig.x, cashPointConfig.y), new Point(cashPointConfig.x+4, cashPointConfig.y + 3));

            TicketOffice office = new TicketOffice(eventPublisher, i,segment,cashPointConfig.direction, appConfig.getMinServiceTime(), appConfig.getMaxServiceTime(), logger);
            ticketOffices.add(office);
        }

        // Set ticket offices to the hall
        hall.setTicketOffices(ticketOffices);

        CashPointConfig reservedCashPointConfig = appConfig.getReservCashPointConfig();
        Segment segment = new Segment(new Point(reservedCashPointConfig.x, reservedCashPointConfig.y),
                new Point(reservedCashPointConfig.x+4, reservedCashPointConfig.y + 3));
        TicketOffice reservedTicketOffice = new TicketOffice(eventPublisher,
                3,
                segment,
                reservedCashPointConfig.direction,
                appConfig.getMinServiceTime(),
                appConfig.getMaxServiceTime(),
                logger);

        hall.setReservedTicketOffice(reservedTicketOffice);

        hall.setMoveSpeed(appConfig.getClientSpeed());


        logger.log("Hall setup complete", LogLevel.Info);
    }

    /**
     * Starts the simulation by initiating ticket office threads and the hall simulation.
     */
    public void startSimulation() {
        // Start ticket office threads
        for (TicketOffice office : hall.getTicketOffices()) {
            executorService.submit(office);
        }

        // Start hall simulation
        hallSimulator = new HallSimulator(eventPublisher, appConfig, hall, clientGenerator, logger);
        Thread simulationThread = new Thread(hallSimulator);
        simulationThread.start();
        hallSimulator.start();

        logger.log("Simulation started", LogLevel.Info);
    }

    /**
     * Stops the simulation by closing ticket office threads and halting the hall simulation.
     */
    public void stopSimulation() {
        // Stop ticket office threads
        for (TicketOffice office : hall.getTicketOffices()) {
            office.closeOffice();
        }

        hallSimulator.stop();
        logger.log("Simulation stopped", LogLevel.Warning);
    }

    /**
     * Closes the cash point with the given ID.
     *
     * @param id The ID of the cash point to close.
     * @return The status or result of closing the cash point.
     */
    public int closeCashPoint(int id) {
        return hall.closeTicketOffice(id);
    }

    /**
     * Resumes the simulation by opening the ticket offices and restarting the hall simulation.
     */
    public void resumeSimulation() {
        for (TicketOffice office : hall.getTicketOffices()) {
            office.openOffice();
        }
        hallSimulator.start();
        logger.log("Simulation resumed", LogLevel.Info);
    }

    /**
     * Opens the cash point with the given ID.
     *
     * @param id The ID of the cash point to open.
     * @return The status or result of opening the cash point.
     */
    public int openCashPoint(int id) {
        return hall.openTicketOffice(id);
    }

    public Hall getHall() {
        return hall;
    }

    public void resetCashPoints()
    {
        List<TicketOffice> ticketOffices = new ArrayList<>();
        for (int i = 0; i < appConfig.getCashPointCount(); i++) {
            CashPointConfig cashPointConfig = appConfig.getCashpointConfigs().get(i);

            Segment segment = new Segment(new Point(cashPointConfig.x, cashPointConfig.y), new Point(cashPointConfig.x+4, cashPointConfig.y + 3));

            TicketOffice office = new TicketOffice(eventPublisher, i,segment,cashPointConfig.direction, appConfig.getMinServiceTime(), appConfig.getMaxServiceTime(), logger);
            ticketOffices.add(office);
        }

        hall.setTicketOffices(ticketOffices);
        executorService.shutdownNow();
        executorService = Executors.newCachedThreadPool();


        // Start ticket office threads
        for (TicketOffice office : hall.getTicketOffices()) {
            executorService.submit(office);
        }

    }
}
