package com.railways.railways.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.railways.railways.communication.DTO.GenerationUpdateDTO;
import com.railways.railways.simulation.GenerationPolicy;

import java.util.List;

/**
 * ConfigModel represents the configuration settings for the railway ticketing system.
 * It includes various parameters like the number of cashpoints, entrances, service times, map size, and other operational configurations.
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

    @JsonProperty("generationType")
    public String getGenerationType() {
        return generationPolicy != null ? generationPolicy.getClass().getSimpleName() : null;
    }
    @JsonProperty("generationPolicySerialized")
    public Object getGenerationPolicyForSerialization() {
       return generationPolicy.toJson();
    }
    /**
     * Constructor to initialize the configuration with given parameters.
     *
     * @param generationPolicy the policy for generating the railway system
     * @param mapSize the size of the map for the railway system
     * @param CashPointCount the number of cashpoints in the system
     * @param entranceCount the number of entrances in the system
     * @param minServiceTime the minimum service time per transaction
     * @param maxServiceTime the maximum service time per transaction
     * @param maxPeopleAllowed the maximum number of people allowed in the premises
     * @param clientSpeed the speed of the client interacting with the system
     */
    public ConfigModel( GenerationPolicy generationPolicy,MapSize mapSize ,  int CashPointCount,
                        int entranceCount, int minServiceTime, int maxServiceTime, int maxPeopleAllowed,
                            double clientSpeed) {
        setGenerationPolicy(generationPolicy);
        setCashPointCount(CashPointCount);
        setEntranceCount(entranceCount);
        setMinServiceTime(minServiceTime);
        setMaxServiceTime(maxServiceTime);
        setMaxPeopleAllowed(maxPeopleAllowed);
        setClientSpeed(clientSpeed);
        setMapSize(mapSize);
    }

    /**
     * Gets the current map size.
     *
     * @return the map size as a MapSize object
     */
    public MapSize getMapSize() {
        return mapSize;
    }

    /**
     * Sets the map size by specifying width and height.
     *
     * @param width the width of the map
     * @param height the height of the map
     * @throws IllegalArgumentException if width or height is non-positive
     */
    public void setMapSize(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        this.mapSize = new MapSize(width, height);
    }

    /**
     * Sets the map size using an existing MapSize object.
     *
     * @param mapSize the MapSize object representing the new map size
     * @throws IllegalArgumentException if mapSize is null
     */
    public void setMapSize(MapSize mapSize) {
        if (mapSize == null) {
            throw new IllegalArgumentException("Map size cannot be null.");
        }
        this.mapSize = mapSize;
    }

    /**
     * Sets the generation policy for the railway system.
     *
     * @param generationPolicy the generation policy to be set
     * @throws IllegalArgumentException if generationPolicy is null
     */
    public void setGenerationPolicy(GenerationPolicy generationPolicy) {
        if (generationPolicy == null) {
            throw new IllegalArgumentException("Generation policy cannot be null.");
        }

        this.generationPolicy = generationPolicy;
    }

    /**
     * Gets the current generation policy.
     *
     * @return the current generation policy
     */
    public GenerationPolicy getGenerationPolicy() {
        return generationPolicy;
    }

    /**
     * Gets the number of cashpoints in the system.
     *
     * @return the number of cashpoints
     */
    public int getCashPointCount() {
        return cashpointsCount;
    }

    /**
     * Sets the number of cashpoints in the system.
     *
     * @param counterCount the number of cashpoints to be set
     * @throws IllegalArgumentException if the count is non-positive
     */
    public void setCashPointCount(int counterCount) {
        if (counterCount <= 0) {
            throw new IllegalArgumentException("The number of cashpoints must be greater than zero.");
        }
        this.cashpointsCount = counterCount;
    }

    /**
     * Gets the list of cashpoint configurations.
     *
     * @return the list of cashpoint configurations
     */
    public List<CashPointConfig> getCashpointConfigs() {
        return cashpointConfigs;
    }

    /**
     * Sets the cashpoint configurations.
     *
     * @param cashpointConfigs the new list of cashpoint configurations
     * @throws IllegalArgumentException if the list is null, empty, or doesn't match the cashpoint count
     */
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

    /**
     * Gets the number of entrances in the system.
     *
     * @return the number of entrances
     */
    public int getEntranceCount() {
        return entranceCount;
    }

    /**
     * Sets the number of entrances in the system.
     *
     * @param entranceCount the number of entrances to be set
     * @throws IllegalArgumentException if the entrance count is non-positive
     */
    public void setEntranceCount(int entranceCount) {
        if (entranceCount <= 0) {
            throw new IllegalArgumentException("The number of entrances must be greater than zero.");
        }
        this.entranceCount = entranceCount;
    }

    /**
     * Gets the minimum service time.
     *
     * @return the minimum service time in milliseconds
     */
    public int getMinServiceTime() {
        return minServiceTime;
    }

    /**
     * Sets the minimum service time.
     *
     * @param minServiceTime the new minimum service time in milliseconds
     * @throws IllegalArgumentException if the value is non-positive
     */
    public void setMinServiceTime(int minServiceTime) {
        if (minServiceTime <= 0) {
            throw new IllegalArgumentException("Minimum service time must be greater than zero.");
        }
        this.minServiceTime = minServiceTime;
    }

    /**
     * Gets the maximum service time.
     *
     * @return the maximum service time in milliseconds
     */
    public int getMaxServiceTime() {
        return maxServiceTime;
    }

    /**
     * Sets the maximum service time.
     *
     * @param maxServiceTime the new maximum service time in milliseconds
     * @throws IllegalArgumentException if the value is non-positive or less than the minimum service time
     */
    public void setMaxServiceTime(int maxServiceTime) {
        if (maxServiceTime <= 0 || maxServiceTime < minServiceTime) {
            throw new IllegalArgumentException("Maximum service time must be greater than zero and not less than minimum service time.");
        }
        this.maxServiceTime = maxServiceTime;
    }

    /**
     * Gets the maximum number of people allowed in the premises.
     *
     * @return the maximum number of people allowed
     */
    public int getMaxPeopleAllowed() {
        return maxPeopleAllowed;
    }

    /**
     * Sets the maximum number of people allowed in the premises.
     *
     * @param maxPeopleAllowed the new maximum number of people allowed
     * @throws IllegalArgumentException if the value is non-positive
     */
    public void setMaxPeopleAllowed(int maxPeopleAllowed) {
        if (maxPeopleAllowed <= 0) {
            throw new IllegalArgumentException("The maximum number of people allowed must be greater than zero.");
        }
        this.maxPeopleAllowed = maxPeopleAllowed;
    }

    /**
     * Updates all configuration fields from a new configuration model.
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

    /**
     * Sets the list of entrance configurations.
     *
     * @param entranceConfigs the new list of entrance configurations
     * @throws IllegalArgumentException if the list is null, empty, or doesn't match the entrance count
     */
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

    /**
     * Gets the list of entrance configurations.
     *
     * @return the list of entrance configurations
     */
    public List<EntranceConfig> getEntranceConfigs() {
        return entranceConfigs;
    }

    /**
     * Sets the reserved cash point configuration.
     *
     * @param reservCashPointConfig the configuration for the reserved cash point
     * @throws IllegalArgumentException if the configuration is null
     */
    public void setReservCashPointConfig(CashPointConfig reservCashPointConfig) {
        if (reservCashPointConfig == null) {
            throw new IllegalArgumentException(
                    "ReserveCashPoint config cannot be null.");
        }

        this.reservCashPointConfig = reservCashPointConfig;
    }

    /**
     * Gets the reserved cash point configuration.
     *
     * @return the reserved cash point configuration
     */
    public CashPointConfig getReservCashPointConfig() {
        return reservCashPointConfig;
    }

    /**
     * Gets the speed of the client.
     *
     * @return the client speed
     */
    public double getClientSpeed()
    {
        return clientSpeed;
    }

    /**
     * Sets the speed of the client.
     *
     * @param newClientSpeed the new client speed
     * @throws IllegalArgumentException if the speed is non-positive
     */
    public void  setClientSpeed(double newClientSpeed)
    {
        if (newClientSpeed <= 0) {
            throw new IllegalArgumentException(
                    "Client speed must be greater than zero.");
        }
        clientSpeed = newClientSpeed;
    }

    /**
     * Gets the X size of the cashpoint.
     *
     * @return the X size of the cashpoint
     */
    public int getCashPointXSize() {
        return cashPointXSize;
    }

    /**
     * Gets the Y size of the cashpoint.
     *
     * @return the Y size of the cashpoint
     */
    public int getCashPointYSize() {
        return cashPointYSize;
    }

    /**
     * Sets the size of the cashpoint.
     *
     * @param x the X size of the cashpoint
     * @param y the Y size of the cashpoint
     * @throws IllegalArgumentException if the sizes are non-positive
     */
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
