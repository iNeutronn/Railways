package com.railways.railways.controllers;

import com.railways.railways.simulation.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * SimulationController handles HTTP requests for controlling the simulation.
 * It provides endpoints for starting, stopping, resuming the simulation, and managing cashpoints.
 */
@RestController
@RequestMapping("/api/simulation")
public class SimulationController {
    // The service that handles simulation logic
    private final SimulationService simulationService;
    /**
     * Constructs a SimulationController with the provided SimulationService.
     *
     * @param simulationService the service that controls the simulation
     */
    @Autowired
    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    /**
     * Starts the simulation.
     *
     * @return a message indicating that the simulation has started
     */
    @GetMapping("/start")
    public String startSimulation() {
        simulationService.startSimulation();
        return "Simulation started!";
    }

    /**
     * Stops the simulation.
     *
     * @return a message indicating that the simulation has stopped
     */
    @GetMapping("/stop")
    public String pauseSimulation() {
        simulationService.stopSimulation();
        return "Simulation stopped!";
    }

    /**
     * Closes the cashpoint with the given ID.
     *
     * @param id the ID of the cashpoint to be closed
     * @return the status code indicating the result of closing the cashpoint
     */
    @PostMapping("/cashpoint/close")
    public int cashPointClosed(@RequestParam int id) {
        return simulationService.closeCashPoint(id);
    }

    /**
     * Resumes the simulation after it has been paused.
     *
     * @return a message indicating that the simulation has resumed
     */
    @GetMapping("/resume")
    public String resumeSimulation() {
        simulationService.resumeSimulation();
        return "Simulation resumed!";
    }

    /**
     * Opens the cashpoint with the given ID.
     *
     * @param id the ID of the cashpoint to be opened
     * @return the status code indicating the result of opening the cashpoint
     */
    @PostMapping("/cashpoint/open")
    public int cashPointResume(@RequestParam int id) {
        return simulationService.openCashPoint(id);
    }
}
