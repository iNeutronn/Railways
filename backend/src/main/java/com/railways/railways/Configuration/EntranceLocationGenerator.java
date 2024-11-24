package com.railways.railways.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EntranceLocationGenerator {
    private int xMapSize;
    private int yMapSize;
    private int xEntrance;
    private int yEntrance;
    private final List<EntranceConfig> possibleLocations = new ArrayList<>();
    private final Random random = new Random();


    public EntranceLocationGenerator(int xMapSize, int yMapSize, int xEntrance, int yEntrance) {
        validateArguments(xMapSize, yMapSize, xEntrance, yEntrance);

        this.xMapSize = xMapSize;
        this.yMapSize = yMapSize;
        this.xEntrance = xEntrance;
        this.yEntrance = yEntrance;

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
        for (int x = xEntrance; x + xEntrance <= xMapSize - xEntrance; x += xEntrance) {
            EntranceConfig config = new EntranceConfig();
            config.x = x;
            config.y = yMapSize - yEntrance;
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
        return xMapSize;
    }

    public void setXMapSize(int xMapSize) {
        validateArguments(xMapSize, yMapSize, xEntrance, yEntrance);
        this.xMapSize = xMapSize;
    }

    public int getYMapSize() {
        return yMapSize;
    }

    public void setYMapSize(int yMapSize) {
        validateArguments(xMapSize, yMapSize, xEntrance, yEntrance);
        this.yMapSize = yMapSize;
    }

    public int getXCashPoint() {
        return xEntrance;
    }

    public void setXCashPoint(int xEntrance) {
        validateArguments(xMapSize, yMapSize, xEntrance, yEntrance);
        this.xEntrance = xEntrance;
    }

    public int getYCashPoint() {
        return yEntrance;
    }

    public void setYCashPoint(int yEntrance) {
        validateArguments(xMapSize, yMapSize, xEntrance, yEntrance);
        this.yEntrance = yEntrance;
    }

    public List<EntranceConfig> getPossibleLocations() {
        return new ArrayList<>(possibleLocations);
    }
}
