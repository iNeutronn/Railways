package com.railways.railways.simulator.src.hall;

import java.awt.*;

public class RegularClientGenerator extends ClientGenerator {
    private final float everyNSeconds; // Interval between generations

    public RegularClientGenerator(float everyNSeconds) {
        this.everyNSeconds = everyNSeconds;
    }

    @Override
    public Client Generate() {
        if (!isStopped) {
            lastGeneratedAt++;
            //notify the UI
            return new Client(1,"Viktor",
                    "TheGreatest",12,new Point(0,0)); // hardcoded example
        }
        return null;
    }
}
