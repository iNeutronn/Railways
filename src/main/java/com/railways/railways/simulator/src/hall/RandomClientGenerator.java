package com.railways.railways.simulator.src.hall;
import java.awt.*;
import java.util.Random;

public class RandomClientGenerator extends ClientGenerator {
    private float everyNSecondsMin;
    private float everyNSecondsMax;
    private final Random random = new Random();


    public RandomClientGenerator(float everyNSecondsMin, float everyNSecondsMax) {
        this.everyNSecondsMin = everyNSecondsMin;
        this.everyNSecondsMax = everyNSecondsMax;
    }

    @Override
    public Client Generate() {
        if (!isStopped) {
            lastGeneratedAt++;
            float interval = everyNSecondsMin + random.nextFloat() * (everyNSecondsMax - everyNSecondsMin);
            // notify the UI
            return new Client(1, "Viktor",
                    "TheGreatest", 12, new Point(0, 0)); // hardcoded example
        }
        return null;
    }
}