package com.railways.railways.Configuration;

import com.railways.railways.domain.station.Direction;

/**
 * Represents the configuration of a cash point in the system.
 *
 * The {@code CashPointConfig} class contains the coordinates and direction of a cash point.
 * It is used to define the placement and orientation of cash points within the station layout.
 *
 */
public class CashPointConfig {
    /**
     * The unique identifier of the cash point.
     */
    public int id;
    /**
     * The x-coordinate of the cash point in the station layout.
     * This represents the horizontal position.
     */
    public int x;
    /**
     * The y-coordinate of the cash point in the station layout.
     * This represents the vertical position.
     */
    public int y;
    /**
     * The direction the cash point is facing.
     * This value is represented by a {@link Direction} enum, which defines the possible orientations.
     */
    public Direction direction;
}
