package com.railways.railways.domain;

import com.railways.railways.Configuration.ConfigModel;
import org.springframework.stereotype.Component;

/**
 * ClientMovementHelper is a helper class that calculates the movement time and distance
 * for clients based on their speed and coordinates.
 */
@Component
public class ClientMovementHelper {
    private final double movementSpeed;

    /**
     * Constructor for ClientMovementHelper.
     *
     * @param config the configuration model containing the client speed
     */
    public ClientMovementHelper(ConfigModel config) {
        movementSpeed = config.getClientSpeed();
    }

    /**
     * Calculates the movement time for a client from the entrance to the cash point.
     *
     * @param entranceX the x-coordinate of the entrance
     * @param entranceY the y-coordinate of the entrance
     * @param cashPointX the x-coordinate of the cash point
     * @param cashPointY the y-coordinate of the cash point
     * @return the movement time for the client
     */
    public double getMovementTime(int entranceX, int entranceY, int cashPointX, int cashPointY) {
        return getDistance(entranceX, entranceY, cashPointX, cashPointY) / movementSpeed;
    }

    /**
     * Calculates the distance between two points.
     *
     * @param x1 the x-coordinate of the first point
     * @param y1 the y-coordinate of the first point
     * @param x2 the x-coordinate of the second point
     * @param y2 the y-coordinate of the second point
     * @return the distance between the two points
     */
    public static double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}