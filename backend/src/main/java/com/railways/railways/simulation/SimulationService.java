package com.railways.railways.simulation;

import com.railways.railways.Configuration.CashPointConfig;
import com.railways.railways.Configuration.ConfigModel;
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

@Service
public class SimulationService {

    private final Hall hall;
    private final ExecutorService executorService;
    private final ClientGenerator clientGenerator;
    private HallSimulator hallSimulator;
    private final ApplicationEventPublisher eventPublisher;
    private final ConfigModel appConfig;

    private final Logger logger;

    public SimulationService(ApplicationEventPublisher eventPublisher, ConfigModel appConfig, Logger logger) {
        this.eventPublisher = eventPublisher;
        this.appConfig = appConfig;
        this.hall = new Hall(appConfig.getMapSize(), logger);
        this.executorService = Executors.newCachedThreadPool(); // Flexible thread pool
        Map<PrivilegeEnum, Integer> privilegeMap = Map.of(
                PrivilegeEnum.DEFAULT, 70,
                PrivilegeEnum.WARVETERAN, 10,
                PrivilegeEnum.WITHCHILD, 10,
                PrivilegeEnum.DISABLED, 10
        );
        this.clientGenerator = new ClientGenerator(privilegeMap);
        this.logger = logger;

        setupHall();
    }

    private void setupHall() {
        hall.setApplicationEventPublisher(eventPublisher);

        // Create ticket offices
        List<TicketOffice> ticketOffices = new ArrayList<>();
        for (int i = 0; i < appConfig.getCashPointCount(); i++) {
             CashPointConfig cashPointConfig = appConfig.getCashpointConfigs().get(i);

            Segment segment = new Segment(new Point(cashPointConfig.x, cashPointConfig.y), new Point(cashPointConfig.x+4, cashPointConfig.y + 3));

            TicketOffice office = new TicketOffice(eventPublisher, i,segment,cashPointConfig.direction, appConfig.getMinServiceTime(), appConfig.getMaxServiceTime());
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
                appConfig.getMaxServiceTime());

        hall.setReservedTicketOffice(reservedTicketOffice);

        hall.setMoveSpeed(appConfig.getClientSpeed());

        // Configure entrances
        List<Entrance> entrances = new ArrayList<>();
        entrances.add(new Entrance(1, new Segment(new Point(0, 0), new Point(0, 1))));
        entrances.add(new Entrance(2, new Segment(new Point(9, 9), new Point(10, 9))));

        hall.setEntrances(entrances);

        logger.log("Hall setup complete", LogLevel.Info);
    }

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

    public void stopSimulation() {
        // Stop ticket office threads
        for (TicketOffice office : hall.getTicketOffices()) {
            office.closeOffice();
        }

        hallSimulator.stop();
        logger.log("Simulation stopped", LogLevel.Warning);
    }

    public int closeCashPoint(int id) {
        return hall.closeTicketOffice(id);
    }

    public void resumeSimulation() {
        for (TicketOffice office : hall.getTicketOffices()) {
            office.openOffice();
        }
        hallSimulator.start();
        logger.log("Simulation resumed", LogLevel.Info);
    }

    public int openCashPoint(int id) {
        return hall.openTicketOffice(id);
    }
}
