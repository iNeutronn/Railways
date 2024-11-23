package com.railways.railways.Configuration;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ConfigController provides REST API to access and modify the configuration of the railway ticketing system.
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    private final ConfigModel configModel;

    /**
     * Constructor for ConfigController, accepting a configuration model.
     *
     * @param configModel the configuration for the railway ticketing system
     */
    public ConfigController(ConfigModel configModel) {
        this.configModel = configModel;
    }

    /**
     * Endpoint to retrieve the current configuration.
     *
     * @return the current configuration as a ConfigModel object
     */
    @GetMapping
    public ConfigModel getConfig() {
        return configModel;
    }

    /**
     * Endpoint to update the entire configuration.
     *
     * @param newConfig the new configuration model
     */
    @PutMapping
    public void updateConfig(@RequestBody ConfigModel newConfig) {
        configModel.updateConfig(newConfig);
    }

    /**
     * Endpoint to update the number of cashpoints.
     *
     * @param cashpointsCount the new number of cashpoints
     */
    @PutMapping("/cashpointsCount")
    public void updateCashpointsCount(@RequestParam int cashpointsCount) {
        configModel.setCashPointCount(cashpointsCount);
    }

    /**
     * Endpoint to update cashpoint locations.
     *
     * @param cashpointConfigs the new cashpoint locations
     */
    @PutMapping("/cashpointConfigs")
    public void updateCashpointLocations(@RequestBody List<CashPointConfig> cashpointConfigs) {
        configModel.setCashpointConfigs(cashpointConfigs);
    }

    /**
     * Endpoint to update the number of entrances.
     *
     * @param entranceCount the new number of entrances
     */
    @PutMapping("/entranceCount")
    public void updateEntranceCount(@RequestParam int entranceCount) {
        configModel.setEntranceCount(entranceCount);
    }

    /**
     * Endpoint to update the minimum service time.
     *
     * @param minServiceTime the new minimum service time
     */
    @PutMapping("/minServiceTime")
    public void updateMinServiceTime(@RequestParam int minServiceTime) {
        configModel.setMinServiceTime(minServiceTime);
    }

    /**
     * Endpoint to update the maximum service time.
     *
     * @param maxServiceTime the new maximum service time
     */
    @PutMapping("/maxServiceTime")
    public void updateMaxServiceTime(@RequestParam int maxServiceTime) {
        configModel.setMaxServiceTime(maxServiceTime);

    }

    /**
     * Endpoint to update the maximum number of people allowed.
     *
     * @param maxPeopleAllowed the new maximum number of people allowed
     */
    @PutMapping("/maxPeopleAllowed")
    public void updateMaxPeopleAllowed(@RequestParam int maxPeopleAllowed) {
        configModel.setMaxPeopleAllowed(maxPeopleAllowed);
    }
}
