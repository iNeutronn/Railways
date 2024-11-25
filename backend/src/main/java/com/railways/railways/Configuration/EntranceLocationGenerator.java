package com.railways.railways.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EntranceLocationGenerator {
    private MapSize mapSize;
    private final int xEntrance;
    private final int yEntrance;
    private final List<EntranceConfig> possibleLocations = new ArrayList<>();
    private final Random random = new Random();



    public EntranceLocationGenerator(MapSize mapSize, int xEntranceSize, int yEntranceSize) {
        validateArguments(mapSize.getWidth(), mapSize.getHeight(), xEntranceSize, yEntranceSize);

        this.mapSize = mapSize;
        this.xEntrance = xEntranceSize;
        this.yEntrance = yEntranceSize;

        generateAllPossibleLocations();
    }

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

    private void generateAllPossibleLocations() {
        int id = 0;
        for (int x = xEntrance; x + xEntrance <= mapSize.getWidth() - xEntrance; x += xEntrance) {
            EntranceConfig config = new EntranceConfig();
            config.id = id++;
            config.x = x;
            config.y = mapSize.getHeight() - yEntrance;
            possibleLocations.add(config);
        }
    }


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

        return result.subList(0, count);
    }

    public int getXMapSize() {
        return mapSize.getWidth();
    }

    public int getYMapSize() {
        return mapSize.getHeight();
    }

    public int getXCashPoint() {
        return xEntrance;
    }

    public int getYCashPoint() {
        return yEntrance;
    }

    public List<EntranceConfig> getPossibleLocations() {
        return new ArrayList<>(possibleLocations);
    }

    public void updateMapSize(MapSize mapSize) {
        validateArguments(mapSize.getWidth(), mapSize.getHeight(), xEntrance, yEntrance);
        this.mapSize = mapSize;
        generateAllPossibleLocations();
    }
}
