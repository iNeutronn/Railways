package com.railways.railways.simulation;

import com.google.gson.Gson;

/**
 * Represents a time-based generation policy for client creation in the simulation.
 * This policy generates clients at a fixed interval (in seconds).
 */
public class IntervalGenerationPolicy implements GenerationPolicy {
    private final Double time;

    /**
     * Constructor to create an IntervalPolicy with a specified time interval.
     *
     * @param time The time interval (in seconds) between client generations.
     */
    public IntervalGenerationPolicy(Double time) {
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

    /**
     * Returns a JSON representation of the IntervalGenerationPolicy.
     *
     * @return A JSON string representing the IntervalGenerationPolicy.
     */
    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
