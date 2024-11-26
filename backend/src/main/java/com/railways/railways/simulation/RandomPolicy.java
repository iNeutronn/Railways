package com.railways.railways.simulation;

import java.util.Random;

/**
 * Represents a random generation policy for client creation in the simulation.
 * This policy generates clients at random intervals between a minimum and maximum time (in seconds).
 */
public class RandomPolicy implements GenerationPolicy {
    private final Double minTime;
    private final Double maxTime;
    private final Random random = new Random();

    /**
     * Constructor to create a RandomPolicy with specified minimum and maximum time intervals.
     *
     * @param minTime The minimum time interval (in seconds) between client generations.
     * @param maxTime The maximum time interval (in seconds) between client generations.
     */
    public RandomPolicy(Double minTime, Double maxTime) {
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    /**
     * Returns a random time interval (in seconds) between client generations.
     * The interval is randomly selected between minTime and maxTime.
     *
     * @return A randomly selected time interval in seconds.
     */
    @Override
    public Double getSeconds() {
        return minTime + (maxTime - minTime) * random.nextDouble();
    }
}
