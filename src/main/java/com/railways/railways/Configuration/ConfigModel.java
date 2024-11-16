package com.railways.railways.Configuration;

import java.util.List;

/**
 * ConfigModel represents the configuration for the railway ticketing system.
 */
public class ConfigModel {

    // Number of ticket cashpoints
    private int cashpointsCount;

    // Coordinates of cashpoint (represented as pairs of X, Y values)
    private List<int[]> cashpointLocations;

    // Number of entrances in the premises
    private int entranceCount;

    // Allowed range of service time per ticket (in milliseconds)
    private int minServiceTime;
    private int maxServiceTime;

    // Maximum number of people allowed in the premises
    private int maxPeopleAllowed;

    public ConfigModel(int CashPointCount, List<int[]> cashpointLocations, int entranceCount, int minServiceTime, int maxServiceTime, int maxPeopleAllowed) {
        setCashPointCount(CashPointCount);
        setCashpointLocations(cashpointLocations);
        setEntranceCount(entranceCount);
        setMinServiceTime(minServiceTime);
        setMaxServiceTime(maxServiceTime);
        setMaxPeopleAllowed(maxPeopleAllowed);
    }

    public int getCashPointCount() {
        return cashpointsCount;
    }

    public void setCashPointCount(int counterCount) {
        if (counterCount <= 0) {
            throw new IllegalArgumentException("The number of cashpoints must be greater than zero.");
        }
        this.cashpointsCount = counterCount;
    }

    public List<int[]> getCashpointLocations() {
        return cashpointLocations;
    }

    public void setCashpointLocations(List<int[]> cashpointLocations) {
        if (cashpointLocations == null || cashpointLocations.isEmpty()) {
            throw new IllegalArgumentException("Cashpoint locations cannot be null or empty.");
        }
        this.cashpointLocations = cashpointLocations;
    }

    public int getEntranceCount() {
        return entranceCount;
    }

    public void setEntranceCount(int entranceCount) {
        if (entranceCount <= 0) {
            throw new IllegalArgumentException("The number of entrances must be greater than zero.");
        }
        this.entranceCount = entranceCount;
    }

    public int getMinServiceTime() {
        return minServiceTime;
    }

    public void setMinServiceTime(int minServiceTime) {
        if (minServiceTime <= 0) {
            throw new IllegalArgumentException("Minimum service time must be greater than zero.");
        }
        this.minServiceTime = minServiceTime;
    }

    public int getMaxServiceTime() {
        return maxServiceTime;
    }

    public void setMaxServiceTime(int maxServiceTime) {
        if (maxServiceTime <= 0 || maxServiceTime < minServiceTime) {
            throw new IllegalArgumentException("Maximum service time must be greater than zero and not less than minimum service time.");
        }
        this.maxServiceTime = maxServiceTime;
    }

    public int getMaxPeopleAllowed() {
        return maxPeopleAllowed;
    }

    public void setMaxPeopleAllowed(int maxPeopleAllowed) {
        if (maxPeopleAllowed <= 0) {
            throw new IllegalArgumentException("The maximum number of people allowed must be greater than zero.");
        }
        this.maxPeopleAllowed = maxPeopleAllowed;
    }

    @Override
    public String toString() {
        return "ConfigModel{" +
                "cashpointsCount=" + cashpointsCount +
                ", cashpointLocations=" + cashpointLocations +
                ", entranceCount=" + entranceCount +
                ", minServiceTime=" + minServiceTime +
                ", maxServiceTime=" + maxServiceTime +
                ", maxPeopleAllowed=" + maxPeopleAllowed +
                '}';
    }
}
