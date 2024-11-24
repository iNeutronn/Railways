package com.railways.railways.Configuration;

import com.railways.railways.domain.station.Direction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class for generating cash point locations on a map.
 * Cash points can only be placed on the left, right, or top wall of the map.
 */
public class CashPointLocationGenerator {
    private int xMapSize = 100;
    private int yMapSize = 70;
    private int xCashPoint = 5;
    private int yCashPoint = 3;

    /**
     * Default constructor with default map and cash point sizes.
     */
    public CashPointLocationGenerator() {
    }

    /**
     * Constructor to initialize the map size and cash point dimensions.
     *
     * @param xMapSize   the width of the map
     * @param yMapSize   the height of the map
     * @param xCashPoint the width of a cash point
     * @param yCashPoint the height of a cash point
     */
    public CashPointLocationGenerator(int xMapSize, int yMapSize, int xCashPoint, int yCashPoint) {
        this.xMapSize = xMapSize;
        this.yMapSize = yMapSize;
        this.xCashPoint = xCashPoint;
        this.yCashPoint = yCashPoint;
    }

    /**
     * Constructor to initialize the map size, with default cash point dimensions.
     *
     * @param xMapSize the width of the map
     * @param yMapSize the height of the map
     */
    public CashPointLocationGenerator(int xMapSize, int yMapSize) {
        this.xMapSize = xMapSize;
        this.yMapSize = yMapSize;
    }

    /**
     * Generates a list of cash point configurations for the specified number of cash points.
     * Cash points are placed on the top, left, or right wall of the map without overlapping.
     *
     * @param cashCount the number of cash points to generate
     * @return a list of cash point configurations
     * @throws IllegalArgumentException if there is insufficient space for the requested number of cash points
     */
    public List<CashPointConfig> generate(int cashCount) {
        int maxTopSpace = (xMapSize / xCashPoint)-1;//-1 to avoid placing cash point at the corner
        int maxLeftSpace = (yMapSize / yCashPoint)-1;//-1 to avoid placing cash point at the corner
        int maxRightSpace = maxLeftSpace;
        int maxTotal = maxTopSpace + maxLeftSpace + maxRightSpace;

        if (cashCount > maxTotal) {
            throw new IllegalArgumentException("Insufficient space for the requested number of cash points.");
        }

        List<CashPointConfig> cashPoints = new ArrayList<>();
        Set<String> occupied = new HashSet<>();

        // Generate top wall cash points
        for (int i = 1; i <= Math.min(cashCount, maxTopSpace); i++) { // Start from 1 to avoid placing cash point at the corner
            int x = i * xCashPoint;
            int y = 0;
            addCashPoint(cashPoints, occupied, x, y, Direction.Up);
        }
        cashCount -= Math.min(cashCount, maxTopSpace);

        // Generate left wall cash points
        for (int i = 0; i < Math.min(cashCount, maxLeftSpace); i++) {
            int x = 0;
            int y = i * yCashPoint;
            addCashPoint(cashPoints, occupied, x, y, Direction.Left);
        }
        cashCount -= Math.min(cashCount, maxLeftSpace);

        // Generate right wall cash points
        for (int i = 0; i < cashCount; i++) {
            int x = xMapSize - xCashPoint;
            int y = i * yCashPoint;
            addCashPoint(cashPoints, occupied, x, y, Direction.Right);
        }

        return cashPoints;
    }

    private void addCashPoint(List<CashPointConfig> cashPoints, Set<String> occupied, int x, int y, Direction direction) {
        String key = x + "," + y;
        if (!occupied.contains(key)) {
            CashPointConfig cashPoint = new CashPointConfig();
            cashPoint.x = x;
            cashPoint.y = y;
            cashPoint.direction = direction;
            cashPoints.add(cashPoint);
            occupied.add(key);
        }
    }

    // Getters and setters

    public int getXMapSize() {
        return xMapSize;
    }

    public void setXMapSize(int xMapSize) {
        this.xMapSize = xMapSize;
    }

    public int getYMapSize() {
        return yMapSize;
    }

    public void setYMapSize(int yMapSize) {
        this.yMapSize = yMapSize;
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
}
