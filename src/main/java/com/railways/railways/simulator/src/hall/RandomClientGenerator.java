package com.railways.railways.simulator.src.hall;

import java.awt.*;
import java.util.Random;

public class RandomClientGenerator extends ClientGenerator {
    private final float everyNSecondsMin;
    private final float everyNSecondsMax;
    private final Random random = new Random();

    public RandomClientGenerator(float everyNSecondsMin, float everyNSecondsMax) {
        this.everyNSecondsMin = everyNSecondsMin;
        this.everyNSecondsMax = everyNSecondsMax;
    }

    @Override
    public Client Generate() {
        if (!isStopped) {
            lastGeneratedAt++;

            // Calculate random interval in milliseconds
            float intervalInSeconds = everyNSecondsMin + random.nextFloat() * (everyNSecondsMax - everyNSecondsMin);
            long intervalInMillis = (long) (intervalInSeconds * 1000);

            try {
                // Introduce the random delay
                Thread.sleep(intervalInMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null; // Thread was interrupted during sleep
            }

            // Return a hardcoded example Client
            return new Client(1, "Viktor",
                    "TheGreatest", 12, new Point(0, 0));
        }
        return null;
    }
}
