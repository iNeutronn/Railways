package com.railways.railways.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * EntranceLocationGenerator generates possible entrance locations based on the given map size and entrance size.
 * It ensures that the entrance configurations fit within the map dimensions and can return random or sequential entrance locations.
 */
public class EntranceLocationGenerator {
    private MapSize mapSize;
    private final int xEntrance;
    private final int yEntrance;
    private final List<EntranceConfig> possibleLocations = new ArrayList<>();
    private final Random random = new Random();

    /**
     * Constructor for EntranceLocationGenerator.
     * Validates input dimensions and generates all possible entrance locations based on the map size and entrance size.
     *
     * @param mapSize the size of the map
     * @param xEntranceSize the width of the entrance
     * @param yEntranceSize the height of the entrance
     */
    public EntranceLocationGenerator(MapSize mapSize, int xEntranceSize, int yEntranceSize) {
        validateArguments(mapSize.getWidth(), mapSize.getHeight(), xEntranceSize, yEntranceSize);

        this.mapSize = mapSize;
        this.xEntrance = xEntranceSize;
        this.yEntrance = yEntranceSize;

        generateAllPossibleLocations();
    }

    /**
     * Validates the input arguments for the map size and entrance size.
     *
     * @param xMapSize the width of the map
     * @param yMapSize the height of the map
     * @param xEntrance the width of the entrance
     * @param yEntrance the height of the entrance
     * @throws IllegalArgumentException if any of the dimensions are invalid
     */
    private void validateArguments(int xMapSize, int yMapSize, int xEntrance, int yEntrance) {
        if (xMapSize <= 0 || yMapSize <= 0) {
            throw new IllegalArgumentException("Map dimensions must be greater than zero.");
        }

        if (xEntrance <= 0 || yEntrance <= 0) {
            throw new IllegalArgumentException("Entrance dimensions must be greater than zero.");
        }

        if (xEntrance > xMapSize || yEntrance > yMapSize) {
            throw new IllegalArgumentException("Entrance dimensions must fit within the map dimensions.");
        }
    }

    /**
     * Generates all possible entrance locations based on the map size and entrance size.
     * Entrance configurations are stored in the `possibleLocations` list.
     */
    private void generateAllPossibleLocations() {
        possibleLocations.clear();
        int id = 0;
        for (int x = xEntrance; x + xEntrance <= mapSize.getWidth() - xEntrance; x += xEntrance) {
            EntranceConfig config = new EntranceConfig();
            config.id = id++;
            config.x = x;
            config.y = mapSize.getHeight() - yEntrance;
            possibleLocations.add(config);
        }
    }

    /**
     * Gets a specified number of entrance locations.
     *
     * @param count the number of entrance locations to retrieve
     * @param randomOrder whether to shuffle the entrance locations randomly
     * @return a list of entrance configurations
     * @throws IllegalArgumentException if the count is less than or equal to zero or exceeds available locations
     */
    public List<EntranceConfig> getLocations(int count, boolean randomOrder) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be greater than zero.");
        }
        if (count > possibleLocations.size()) {
            throw new IllegalArgumentException("Not enough locations available.");
        }

        List<EntranceConfig> result = new ArrayList<>(possibleLocations);
        if (randomOrder) {
            Collections.shuffle(result, random);
        }

        var sublist = result.subList(0, count);
        possibleLocations.removeAll(sublist);

        return result.subList(0, count);
    }

    /**
     * Gets the width of the map.
     *
     * @return the map's width
     */
    public int getXMapSize() {
        return mapSize.getWidth();
    }

    /**
     * Gets the height of the map.
     *
     * @return the map's height
     */
    public int getYMapSize() {
        return mapSize.getHeight();
    }

    /**
     * Gets the entrance's X size (width).
     *
     * @return the width of the entrance
     */
    public int getXCashPoint() {
        return xEntrance;
    }

    /**
     * Gets the entrance's Y size (height).
     *
     * @return the height of the entrance
     */
    public int getYCashPoint() {
        return yEntrance;
    }

    /**
     * Gets the list of all possible entrance locations.
     *
     * @return a list of possible entrance configurations
     */
    public List<EntranceConfig> getPossibleLocations() {
        return new ArrayList<>(possibleLocations);
    }

    /**
     * Updates the map size and regenerates the possible entrance locations.
     *
     * @param mapSize the new map size
     * @throws IllegalArgumentException if the new map size is invalid
     */
    public void updateMapSize(MapSize mapSize) {
        validateArguments(mapSize.getWidth(), mapSize.getHeight(), xEntrance, yEntrance);
        this.mapSize = mapSize;
        generateAllPossibleLocations();
    }
}
