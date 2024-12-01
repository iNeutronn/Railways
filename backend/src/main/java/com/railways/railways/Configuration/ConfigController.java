package com.railways.railways.Configuration;

import com.railways.railways.simulation.IntervalGenerationPolicy;
import com.railways.railways.simulation.RandomGenerationPolicy;
import com.railways.railways.simulation.SimulationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ConfigController provides a REST API to access and modify the configuration of the railway ticketing system.
 * It includes endpoints for retrieving and updating various configuration settings.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/config")
public class ConfigController {

    private final ConfigModel configModel;
    private final MapGenerationHelper mapGenerationHelper;
    private final SimulationService simulationService;

    /**
     * Constructor for ConfigController, accepting a configuration model.
     *
     * @param configModel the configuration for the railway ticketing system
     * @param mapGenerationHelper the helper for generating and updating map configurations
     */
    public ConfigController(ConfigModel configModel, MapGenerationHelper mapGenerationHelper, SimulationService simulationService) {
        this.configModel = configModel;
        this.mapGenerationHelper = mapGenerationHelper;
        this.simulationService = simulationService;
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
     * @return the updated configuration model
     */
    @PostMapping
    public ConfigModel updateConfig(@RequestBody ConfigModel newConfig) {
        configModel.updateConfig(newConfig);
        return configModel;
    }

    /**
     * Endpoint to update the number of cashpoints.
     *
     * @param cashpointsCount the new number of cashpoints
     * @return the updated configuration model
     */
    @PostMapping("/cashpointsCount")
    public ConfigModel updateCashpointsCount(@RequestParam int cashpointsCount) {
        mapGenerationHelper.updateCashPointConfigs(cashpointsCount);
        simulationService.resetCashPoints();
        return configModel;
    }

    /**
     * Endpoint to get the number of cashpoints.
     *
     * @return the current number of cashpoints
     */
    @GetMapping("/cashpointsCount")
    public int getCashpointsCount() {
        return configModel.getCashPointCount();
    }

    /**
     * Endpoint to update cashpoint locations.
     *
     * @param cashpointConfigs the new cashpoint locations
     * @return the updated configuration model
     */
    @PostMapping("/cashpointConfigs")
    public ConfigModel updateCashpointLocations(@RequestBody List<CashPointConfig> cashpointConfigs) {
        configModel.setCashpointConfigs(cashpointConfigs);
        return configModel;
    }

    /**
     * Endpoint to get the entrance configurations.
     *
     * @return the list of current entrance configurations
     */
    @GetMapping("/entranceConfigs")
    public List<EntranceConfig> getEntranceConfigs() {
        return configModel.getEntranceConfigs();
    }

    /**
     * Endpoint to update the number of entrances.
     *
     * @param entranceCount the new number of entrances
     * @return the updated configuration model
     */
    @PostMapping("/entranceCount")
    public ConfigModel updateEntranceCount(@RequestParam int entranceCount) {
        mapGenerationHelper.updateEntranceConfigs(entranceCount);
        return configModel;
    }

    /**
     * Endpoint to get the minimum service time.
     *
     * @return the current minimum service time
     */
    @GetMapping("/minServiceTime")
    public int getMinServiceTime() {
        return configModel.getMinServiceTime();
    }

    /**
     * Endpoint to update the minimum service time.
     *
     * @param minServiceTime the new minimum service time
     * @return the updated configuration model
     */
    @PostMapping("/minServiceTime")
    public ConfigModel updateMinServiceTime(@RequestParam int minServiceTime) {
        configModel.setMinServiceTime(minServiceTime);
        return configModel;
    }

    /**
     * Endpoint to get the maximum service time.
     *
     * @return the current maximum service time
     */
    @GetMapping("/maxServiceTime")
    public int getMaxServiceTime() {
        return configModel.getMaxServiceTime();
    }

    /**
     * Endpoint to update the maximum service time.
     *
     * @param maxServiceTime the new maximum service time
     * @return the updated configuration model
     */
    @PostMapping("/maxServiceTime")
    public ConfigModel updateMaxServiceTime(@RequestParam int maxServiceTime) {
        configModel.setMaxServiceTime(maxServiceTime);
        return configModel;
    }

    /**
     * Endpoint to get the maximum number of people allowed.
     *
     * @return the current maximum number of people allowed
     */
    @GetMapping("/maxPeopleAllowed")
    public int getMaxPeopleAllowed() {
        return configModel.getMaxPeopleAllowed();
    }

    /**
     * Endpoint to update the maximum number of people allowed.
     *
     * @param maxPeopleAllowed the new maximum number of people allowed
     * @return the updated configuration model
     */
    @PostMapping("/maxPeopleAllowed")
    public ConfigModel updateMaxPeopleAllowed(@RequestParam int maxPeopleAllowed) {
        configModel.setMaxPeopleAllowed(maxPeopleAllowed);
        return configModel;
    }

    /**
     * Endpoint to get the map size.
     *
     * @return the current map size
     */
    @GetMapping("/mapSize")
    public MapSize getMapSize() {
        return configModel.getMapSize();
    }

    /**
     * Endpoint to update the map size.
     *
     * @param width the new width of the map
     * @param height the new height of the map
     * @return the updated configuration model
     */
    @PostMapping("/mapSize")
    public ConfigModel updateMapSize(@RequestParam int width, @RequestParam int height) {
        mapGenerationHelper.updateMapSize(new MapSize(width, height));
        simulationService.resetCashPoints();
        return configModel;
    }

    @PostMapping("/setRandomGenerationPolicy")
    public ConfigModel setGenerationPolicy(@RequestParam double minTime, @RequestParam double maxTime) {
        configModel.setGenerationPolicy(new RandomGenerationPolicy(minTime, maxTime));
        return configModel;
    }

    @PostMapping("/setIntervalGenerationPolicy")
    public ConfigModel setIntervalGenerationPolicy(@RequestParam double interval) {
        configModel.setGenerationPolicy(new IntervalGenerationPolicy(interval));
        return configModel;
    }

    @PostMapping("/setPolicyWithDefaultValues")
    public ConfigModel setPolicyWithDefaultValues(@RequestParam String policy) {
        policy = policy.toLowerCase();
        if (policy.equals("random")) {
            configModel.setGenerationPolicy(new RandomGenerationPolicy(5.0, 10.0));
        } else if (policy.equals("interval")) {
            configModel.setGenerationPolicy(new IntervalGenerationPolicy(7.0));
        }
        return configModel;

    }

}