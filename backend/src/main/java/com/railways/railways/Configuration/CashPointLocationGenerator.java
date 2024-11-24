package com.railways.railways.Configuration;

import com.railways.railways.domain.station.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A class for managing cash point locations on a map.
 * Generates all possible locations for cash points on the top, left, and right walls of the map,
 * excluding corner positions.
 */
public class CashPointLocationGenerator {
    private int xMapSize;
    private int yMapSize;
    private int xCashPoint;
    private int yCashPoint;
    private final List<CashPointConfig> possibleLocations = new ArrayList<>();
    private final Random random = new Random();

    /**
     * Constructor to initialize the map size and cash point dimensions, and generate all possible locations.
     *
     * @param xMapSize   the width of the map
     * @param yMapSize   the height of the map
     * @param xCashPoint the width of a cash point
     * @param yCashPoint the height of a cash point
     * @throws IllegalArgumentException if any argument is invalid
     */
    public CashPointLocationGenerator(int xMapSize, int yMapSize, int xCashPoint, int yCashPoint) {
        validateArguments(xMapSize, yMapSize, xCashPoint, yCashPoint);
        this.xMapSize = xMapSize;
        this.yMapSize = yMapSize;
        this.xCashPoint = xCashPoint;
        this.yCashPoint = yCashPoint;
        generateAllPossibleLocations();
    }

    /**
     * Validates the input arguments.
     *
     * @param xMapSize   the width of the map
     * @param yMapSize   the height of the map
     * @param xCashPoint the width of a cash point
     * @param yCashPoint the height of a cash point
     * @throws IllegalArgumentException if any dimension is invalid
     */
    private void validateArguments(int xMapSize, int yMapSize, int xCashPoint, int yCashPoint) {
        if (xMapSize <= 0 || yMapSize <= 0) {
            throw new IllegalArgumentException("Map dimensions must be greater than zero.");
        }
        if (xCashPoint <= 0 || yCashPoint <= 0) {
            throw new IllegalArgumentException("Cash point dimensions must be greater than zero.");
        }
        if (xCashPoint >= xMapSize || yCashPoint >= yMapSize) {
            throw new IllegalArgumentException("Cash point dimensions must fit within the map dimensions.");
        }
    }

    /**
     * Generates all possible locations for cash points on the top, left, and right walls,
     * excluding corner positions.
     */
    private void generateAllPossibleLocations() {
        // Top wall (excluding corners)
        for (int x = xCashPoint; x + xCashPoint <= xMapSize - xCashPoint; x += xCashPoint) {
            CashPointConfig config = new CashPointConfig();
            config.x = x;
            config.y = 0;
            config.direction = Direction.Down;
            possibleLocations.add(config);
        }

        // Left wall (excluding corners)
        for (int y = yCashPoint; y + yCashPoint <= yMapSize - yCashPoint; y += yCashPoint) {
            CashPointConfig config = new CashPointConfig();
            config.x = 0;
            config.y = y;
            config.direction = Direction.Right;
            possibleLocations.add(config);
        }

        // Right wall (excluding corners)
        for (int y = yCashPoint; y + yCashPoint <= yMapSize - yCashPoint; y += yCashPoint) {
            CashPointConfig config = new CashPointConfig();
            config.x = xMapSize - xCashPoint;
            config.y = y;
            config.direction = Direction.Left;
            possibleLocations.add(config);
        }
    }

    /**
     * Returns a specified number of cash point configurations.
     * If randomOrder is true, the points are returned in random order.
     * Otherwise, they are returned in order: top, left, right.
     *
     * @param count       the number of cash points to return
     * @param randomOrder whether to return points in random order
     * @return a list of cash point configurations
     * @throws IllegalArgumentException if the requested count exceeds available locations
     */
    public List<CashPointConfig> getLocations(int count, boolean randomOrder) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be greater than zero.");
        }
        if (count > possibleLocations.size()) {
            throw new IllegalArgumentException("Not enough locations available.");
        }

        List<CashPointConfig> result = new ArrayList<>(possibleLocations);
        if (randomOrder) {
            Collections.shuffle(result, random);
        }
        return result.subList(0, count);
    }

    // Getters and setters

    public int getXMapSize() {
        return xMapSize;
    }

    public void setXMapSize(int xMapSize) {
        validateArguments(xMapSize, yMapSize, xCashPoint, yCashPoint);
        this.xMapSize = xMapSize;
    }

    public int getYMapSize() {
        return yMapSize;
    }

    public void setYMapSize(int yMapSize) {
        validateArguments(xMapSize, yMapSize, xCashPoint, yCashPoint);
        this.yMapSize = yMapSize;
    }

    public int getXCashPoint() {
        return xCashPoint;
    }

    public void setXCashPoint(int xCashPoint) {
        validateArguments(xMapSize, yMapSize, xCashPoint, yCashPoint);
        this.xCashPoint = xCashPoint;
    }

    public int getYCashPoint() {
        return yCashPoint;
    }

    public void setYCashPoint(int yCashPoint) {
        validateArguments(xMapSize, yMapSize, xCashPoint, yCashPoint);
        this.yCashPoint = yCashPoint;
    }

    public List<CashPointConfig> getPossibleLocations() {
        return new ArrayList<>(possibleLocations);
    }
}
