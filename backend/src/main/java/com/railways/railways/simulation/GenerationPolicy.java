package com.railways.railways.simulation;

/**
 * Interface representing a policy for generating time intervals in the simulation.
 *
 * This interface defines a method to retrieve the time in seconds for generating an event,
 * which can be used to simulate time intervals for operations such as client arrivals,
 * ticket processing, or other timed events in the system.
 */
public interface GenerationPolicy {
    public Double getSeconds();

    String toJson();
}
