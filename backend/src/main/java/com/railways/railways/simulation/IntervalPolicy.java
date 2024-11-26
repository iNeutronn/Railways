package com.railways.railways.simulation;

/**
 * Represents a time-based generation policy for client creation in the simulation.
 * This policy generates clients at a fixed interval (in seconds).
 */
public class IntervalPolicy implements GenerationPolicy {
    private final Double time;

    /**
     * Constructor to create an IntervalPolicy with a specified time interval.
     *
     * @param time The time interval (in seconds) between client generations.
     */
    public IntervalPolicy(Double time) {
        this.time = time;
    }

    /**
     * Returns the time interval (in seconds) for client generation.
     *
     * @return The interval time in seconds.
     */
    @Override
    public Double getSeconds() {
        return time;
    }
}
