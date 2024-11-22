package com.railways.railways.simulator.src.hall;

public class IntervalPolicy implements GenerationPolicy{
    private final Double time;

    public IntervalPolicy(Double time) {
        this.time = time;
    }

    @Override
    public Double getSeconds() {
        return time;
    }
}
