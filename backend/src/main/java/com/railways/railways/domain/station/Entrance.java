package com.railways.railways.domain.station;

import java.awt.*;

/**
 * Represents an entrance to a station or segment within a railway system.
 * An entrance is associated with a specific segment and has an ID for identification.
 */
public class Entrance {
    private int id;
    private Segment segment;

    /**
     * Constructs an Entrance with the specified ID and associated segment.
     *
     * @param id the unique identifier for this entrance
     * @param segment the segment this entrance is linked to
     */
    public Entrance(int id, Segment segment) {
        this.id = id;
        this.segment = segment;
    }

    /**
     * Gets the ID of this entrance.
     *
     * @return the ID of the entrance
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the segment associated with this entrance.
     *
     * @return the segment associated with the entrance
     */
    public Segment getSegment() {
        return segment;
    }

    /**
     * Gets the starting point of the segment associated with this entrance.
     * This point represents the start of the segment for the entrance.
     *
     * @return the starting point of the segment
     */
    public Point getStartPoint() {
        return segment.start;
    }
}