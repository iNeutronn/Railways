package com.railways.railways.Configuration;

import com.railways.railways.domain.station.Direction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * A class for managing the generation and retrieval of cash point locations on a map.
 *
 * This class generates all possible locations for placing cash points along the top, left, and right walls of a map,
 * excluding the corner positions. It provides functionality to validate input dimensions, generate the cash point
 * locations, and retrieve specific numbers of cash points either in ordered or random order.
 *
 */
public class CashPointLocationGenerator {
    /**
     * The map size that defines the dimensions of the map.
     */
    private MapSize mapSize;
    /**
     * The width of a cash point.
     */
    private int xCashPoint;
    /**
     * The height of a cash point.
     */
    private int yCashPoint;
    /**
     * A list of possible locations where cash points can be placed on the map.
     */
    private final List<CashPointConfig> possibleLocations = new ArrayList<>();
    /**
     * A random number generator used for shuffling the locations.
     */
    private final Random random = new Random();

    /**
     * Constructs a {@code CashPointLocationGenerator} instance with the specified map size and cash point dimensions.
     *
     * The constructor validates the provided dimensions and generates all possible locations for cash points.
     *
     * @param mapSize the dimensions of the map
     * @param xCashPoint the width of a cash point
     * @param yCashPoint the height of a cash point
     * @throws IllegalArgumentException if any of the dimensions are invalid
     */
    public CashPointLocationGenerator(MapSize mapSize , int xCashPoint , int yCashPoint) {
        this.mapSize = mapSize;
        this.xCashPoint = xCashPoint;
        this.yCashPoint = yCashPoint;
        validateArguments(mapSize.getWidth(), mapSize.getHeight(), xCashPoint, yCashPoint);
        generateAllPossibleLocations();
    }

    /**
     * Validates the input arguments for the map size and cash point dimensions.
     *
     * Throws an {@code IllegalArgumentException} if any dimension is invalid.
     *
     * @param xMapSize the width of the map
     * @param yMapSize the height of the map
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
     * Generates all possible locations for placing cash points on the top, left, and right walls,
     * excluding the corner positions. Cash points are placed with a specified gap on each wall.
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
     *
     * If the {@code randomOrder} flag is true, the returned locations will be in a random order;
     * otherwise, they will be in the default order: top wall, left wall, right wall.
     *
     * @param count the number of cash point configurations to return
     * @param randomOrder whether to return the points in random order
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

    /**
     * Returns the width of the map.
     *
     * @return the width of the map
     */
    public int getXMapSize() {
        return mapSize.getWidth();
    }

    /**
     * Returns the height of the map.
     *
     * @return the height of the map
     */
    public int getYMapSize() {
        return mapSize.getHeight();
    }

    /**
     * Returns the width of a cash point.
     *
     * @return the width of a cash point
     */
    public int getXCashPoint() {
        return xCashPoint;
    }

    /**
     * Sets the width of a cash point.
     *
     * @param xCashPoint the new width of the cash point
     */
    public void setXCashPoint(int xCashPoint) {

        this.xCashPoint = xCashPoint;
    }

    /**
     * Returns the height of a cash point.
     *
     * @return the height of a cash point
     */
    public int getYCashPoint() {
        return yCashPoint;
    }

    /**
     * Sets the height of a cash point.
     *
     * @param yCashPoint the new height of the cash point
     */
    public void setYCashPoint(int yCashPoint) {
        this.yCashPoint = yCashPoint;
    }

    /**
     * Returns a list of all possible cash point locations.
     *
     * @return a list of all possible cash point configurations
     */
    public List<CashPointConfig> getPossibleLocations() {
        return new ArrayList<>(possibleLocations);
    }

    /**
     * Updates the map size and regenerates the possible cash point locations.
     *
     * @param mapSize the new map size
     */
    public void updateMapSize(MapSize mapSize) {
        validateArguments(mapSize.getWidth(), mapSize.getHeight(), xCashPoint, yCashPoint);
        this.mapSize = mapSize;
        generateAllPossibleLocations();
    }
}
