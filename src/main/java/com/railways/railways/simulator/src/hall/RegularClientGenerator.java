package com.railways.railways.simulator.src.hall;

public class RegularClientGenerator extends ClientGenerator {
    private float everyNSeconds;

    public RegularClientGenerator(float everyNSeconds) {
        this.everyNSeconds = everyNSeconds;
    }

    @Override
    public Client Generate() {
        if (!isStopped) {
            // Logic for generating regular clients
            lastGeneratedAt++; // Placeholder logic
        }
        return null; // Replace with actual generation logic
    }
}
