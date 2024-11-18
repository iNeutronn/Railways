package com.railways.railways.simulator.src.hall;

public class RandomClientGenerator extends ClientGenerator {
    private float everyNSecondsMin;
    private float everyNSecondsMax;

    public RandomClientGenerator(float everyNSecondsMin, float everyNSecondsMax) {
        this.everyNSecondsMin = everyNSecondsMin;
        this.everyNSecondsMax = everyNSecondsMax;
    }

    @Override
    public Client Generate() {
        if (!isStopped) {
            // Logic for generating random clients
            lastGeneratedAt++; // Placeholder logic
        }
        return null; // Replace with actual generation logic
    }
}