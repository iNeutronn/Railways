package com.railways.railways.simulator.src.hall;

import java.awt.*;

public class RegularClientGenerator extends ClientGenerator {
    private final long intervalInMillis;

    public RegularClientGenerator(float everyNSeconds) {
        this.intervalInMillis =(long) (everyNSeconds * 1000);
    }

    @Override
    public Client Generate() {
        if (!isStopped) {
            try {
                // Затримка між генераціями
                Thread.sleep(intervalInMillis);
            } catch (InterruptedException e) {
                // Обробка можливого переривання потоку
                Thread.currentThread().interrupt();
                System.err.println("Thread was interrupted: " + e.getMessage());
                return null;
            }

            lastGeneratedAt++;
            //notify the UI
            return new Client(1,"Viktor",
                    "TheGreatest",12,new Point(0,0)); // hardcoded example
        }
        return null;
    }
}
