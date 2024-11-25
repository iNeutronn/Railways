package com.railways.railways.Configuration;

import com.railways.railways.domain.station.Direction;
import org.springframework.stereotype.Component;

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
    private final MapSize mapSize;
    private int xCashPoint;
    private int yCashPoint;
    private final List<CashPointConfig> possibleLocations = new ArrayList<>();
    private final Random random = new Random();



    public CashPointLocationGenerator(MapSize mapSize , int xCashPoint , int yCashPoint) {
        this.mapSize = mapSize;
        this.xCashPoint = xCashPoint;
        this.yCashPoint = yCashPoint;
        validateArguments(mapSize.getWidth(), mapSize.getHeight(), xCashPoint, yCashPoint);
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
        int id = 0;
        // Top wall (excluding corners)
        for (int x = xCashPoint; x + xCashPoint <= mapSize.getWidth() - xCashPoint; x += xCashPoint) {
            CashPointConfig config = new CashPointConfig();
            config.id = id++;
            config.x = x;
            config.y = 0;
            config.direction = Direction.Down;
            possibleLocations.add(config);
        }

        // Left wall (excluding corners)
        for (int y = yCashPoint; y + yCashPoint <= mapSize.getHeight() - yCashPoint; y += yCashPoint) {
            CashPointConfig config = new CashPointConfig();
            config.id = id++;
            config.x = 0;
            config.y = y;
            config.direction = Direction.Right;
            possibleLocations.add(config);
        }

        // Right wall (excluding corners)
        for (int y = yCashPoint; y + yCashPoint <= mapSize.getHeight() - yCashPoint; y += yCashPoint) {
            CashPointConfig config = new CashPointConfig();
            config.id = id++;
            config.x = mapSize.getWidth() - xCashPoint;
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
        return mapSize.getWidth();
    }


    public int getYMapSize() {
        return mapSize.getHeight();
    }


    public int getXCashPoint() {
        return xCashPoint;
    }

    public void setXCashPoint(int xCashPoint) {

        this.xCashPoint = xCashPoint;
    }

    public int getYCashPoint() {
        return yCashPoint;
    }

    public void setYCashPoint(int yCashPoint) {
        this.yCashPoint = yCashPoint;
    }

    public List<CashPointConfig> getPossibleLocations() {
        return new ArrayList<>(possibleLocations);
    }
}
