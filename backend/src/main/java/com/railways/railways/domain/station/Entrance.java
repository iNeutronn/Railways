package com.railways.railways.domain.station;

import java.awt.*;

/**
 * Represents an entrance to a station or segment within a railway system.
 * An entrance is associated with a specific segment and has an ID for identification.
 */
public class Entrance {
    private final int id;

    /**
     * Constructs an Entrance with the specified ID and associated segment.
     *
     * @param id the unique identifier for this entrance
     * @param segment the segment this entrance is linked to
     */
    public Entrance(int id) {
        this.id = id;
    }

    /**
     * Gets the ID of this entrance.
     *
     * @return the ID of the entrance
     */
    public int getId() {
        return id;
    }
}