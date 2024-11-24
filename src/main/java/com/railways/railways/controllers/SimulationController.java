package com.railways.railways.controllers;

import com.railways.railways.simulation.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

    private final SimulationService simulationService;

    @Autowired
    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @GetMapping("/start")
    public String resumeSimulation() {
        simulationService.startSimulation();
        return "Simulation started!";
    }

    @GetMapping("/stop")
    public String pauseSimulation() {
        simulationService.stopSimulation();
        return "Simulation stopped!";
    }

    @PostMapping("/cashpoint/close")
    public int cashPointClosed(@RequestParam int id) {
        return simulationService.closeCashPoint(id);
    }

}
