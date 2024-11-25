package com.railways.railways.Configuration;

import com.railways.railways.communication.DTO.GenerationUpdateDTO;
import com.railways.railways.simulation.GenerationPolicy;

import java.util.List;

/**
 * ConfigModel represents the configuration for the railway ticketing system.
 */
public class ConfigModel {

    private transient GenerationPolicy generationPolicy;

    private MapSize mapSize;

    private int cashPointXSize;
    private int cashPointYSize;

    private int hallMaxCapacity;

    // Number of ticket cashpoints
    private int cashpointsCount;

    // Coordinates of cashpoint (represented as pairs of X, Y values)
    private List<CashPointConfig> cashpointConfigs;

    private CashPointConfig reservCashPointConfig;

    // Number of entrances in the premises
    private int entranceCount;

    private List<EntranceConfig> entranceConfigs;

    // Allowed range of service time per ticket (in milliseconds)
    private int minServiceTime;
    private int maxServiceTime;

    // Maximum number of people allowed in the premises
    private int maxPeopleAllowed;

    //speed of client
    private double clientSpeed;

    public ConfigModel( GenerationPolicy generationPolicy,MapSize mapSize ,  int CashPointCount, int entranceCount, int minServiceTime, int maxServiceTime, int maxPeopleAllowed, double clientSpeed) {
        setGenerationPolicy(generationPolicy);
        setCashPointCount(CashPointCount);
        setEntranceCount(entranceCount);
        setMinServiceTime(minServiceTime);
        setMaxServiceTime(maxServiceTime);
        setMaxPeopleAllowed(maxPeopleAllowed);
        setClientSpeed(clientSpeed);
        setMapSize(mapSize);
    }

    public MapSize getMapSize() {
        return mapSize;
    }

    public void setMapSize(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        this.mapSize = new MapSize(width, height);
    }

    public void setMapSize(MapSize mapSize) {
        if (mapSize == null) {
            throw new IllegalArgumentException("Map size cannot be null.");
        }
        this.mapSize = mapSize;
    }

    public void setGenerationPolicy(GenerationPolicy generationPolicy) {
        if (generationPolicy == null) {
            throw new IllegalArgumentException("Generation policy cannot be null.");
        }

        this.generationPolicy = generationPolicy;
    }

    public GenerationPolicy getGenerationPolicy() {
        return generationPolicy;
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

    public List<CashPointConfig> getCashpointConfigs() {
        return cashpointConfigs;
    }

    public void setCashpointConfigs(List<CashPointConfig> cashpointConfigs) {
        if (cashpointConfigs == null || cashpointConfigs.isEmpty()) {
            throw new IllegalArgumentException("Cashpoint configs cannot be null or empty.");
        }
        if (cashpointConfigs.size() != cashpointsCount) {
            throw new IllegalArgumentException(
                    "The size of cashpoint configs does not match cashpointsCount.");
        }

        this.cashpointConfigs = cashpointConfigs;
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

    /**
     * Updates all configuration fields.
     *
     * @param newConfig the new configuration model
     */
    public void updateConfig(ConfigModel newConfig) {
        setCashPointCount(newConfig.getCashPointCount());
        setCashpointConfigs(newConfig.getCashpointConfigs());
        setEntranceCount(newConfig.getEntranceCount());
        setMinServiceTime(newConfig.getMinServiceTime());
        setMaxServiceTime(newConfig.getMaxServiceTime());
        setMaxPeopleAllowed(newConfig.getMaxPeopleAllowed());
    }

    public void setEntranceConfigs(List<EntranceConfig> entranceConfigs) {
        if (entranceConfigs == null || entranceConfigs.isEmpty()) {
            throw new IllegalArgumentException("Entrance configs cannot be null or empty.");
        }

        if (entranceConfigs.size() != entranceCount) {
            throw new IllegalArgumentException
                    ("The size of entrance configs does not match entranceCount.");
        }

        this.entranceConfigs = entranceConfigs;
    }

    public List<EntranceConfig> getEntranceConfigs() {
        return entranceConfigs;
    }

    public void setReservCashPointConfig(CashPointConfig reservCashPointConfig) {
        if (reservCashPointConfig == null) {
            throw new IllegalArgumentException(
                    "ReserveCashPoint config cannot be null.");
        }

        this.reservCashPointConfig = reservCashPointConfig;
    }
    public CashPointConfig getReservCashPointConfig() {
        return reservCashPointConfig;
    }

    public double getClientSpeed()
    {
        return clientSpeed;
    }

    public void  setClientSpeed(double newClientSpeed)
    {
        if (newClientSpeed <= 0) {
            throw new IllegalArgumentException(
                    "Client speed must be greater than zero.");
        }
        clientSpeed = newClientSpeed;
    }

    public int getCashPointXSize() {
        return cashPointXSize;
    }

    public int getCashPointYSize() {
        return cashPointYSize;
    }

    public  void  setCashPointSize(int x, int y)
    {
        if (x <= 0 || y <= 0) {
            throw new IllegalArgumentException(
                    "Cash point size must be greater than zero.");
        }
        cashPointXSize = x;
        cashPointYSize = y;
    }


    @Override
    public String toString() {
        return "ConfigModel{" +
                "cashpointsCount=" + cashpointsCount +
                ", cashpointLocations=" + cashpointConfigs +
                ", entranceCount=" + entranceCount +
                ", minServiceTime=" + minServiceTime +
                ", maxServiceTime=" + maxServiceTime +
                ", maxPeopleAllowed=" + maxPeopleAllowed +
                '}';
    }
}
