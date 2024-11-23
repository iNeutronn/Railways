package com.railways.railways.Configuration;

import java.util.List;

/**
 * ConfigModel represents the configuration for the railway ticketing system.
 */
public class ConfigModel {

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

    public ConfigModel(int CashPointCount, List<CashPointConfig> cashPointConfigs,CashPointConfig reservCashPointConfig ,List<EntranceConfig> entranceConfigs , int entranceCount, int minServiceTime, int maxServiceTime, int maxPeopleAllowed) {
        setCashPointCount(CashPointCount);
        setCashpointConfigs(cashPointConfigs);
        setEntranceCount(entranceCount);
        setMinServiceTime(minServiceTime);
        setMaxServiceTime(maxServiceTime);
        setMaxPeopleAllowed(maxPeopleAllowed);
        setEntranceConfigs(entranceConfigs);
        setReservCashPointConfig(reservCashPointConfig);
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

    public void setEntranceConfigs(List<EntranceConfig> entranceConfigs) {
        this.entranceConfigs = entranceConfigs;
    }

    public List<EntranceConfig> getEntranceConfigs() {
        return entranceConfigs;
    }

    public void setReservCashPointConfig(CashPointConfig reservCashPointConfig) {
        this.reservCashPointConfig = reservCashPointConfig;
    }
    public CashPointConfig getReservCashPointConfig() {
        return reservCashPointConfig;
    }
}
