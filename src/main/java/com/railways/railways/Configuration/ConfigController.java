package com.railways.railways.Configuration;

import org.springframework.web.bind.annotation.*;
import com.railways.railways.simulation.GenerationPolicy;
import java.util.List;

@RestController
@RequestMapping("/config")
public class ConfigController {

    private final ConfigModel configModel;

    public ConfigController(ConfigModel configModel) {
        this.configModel = configModel;
    }

    @GetMapping
    public ConfigModel getConfig() {
        return configModel;
    }

    @PutMapping
    public void updateConfig(@RequestBody ConfigModel newConfig) {
        configModel.updateConfig(newConfig);
    }

    @GetMapping("/generationPolicy")
    public GenerationPolicy getGenerationPolicy() {
        return configModel.getGenerationPolicy();
    }

    @PutMapping("/generationPolicy")
    public void updateGenerationPolicy(@RequestBody GenerationPolicy generationPolicy) {
        configModel.setGenerationPolicy(generationPolicy);
    }

    @GetMapping("/cashpointsCount")
    public int getCashpointsCount() {
        return configModel.getCashPointCount();
    }

    @PutMapping("/cashpointsCount")
    public void updateCashpointsCount(@RequestParam int cashpointsCount) {
        configModel.setCashPointCount(cashpointsCount);
    }

    @GetMapping("/cashpointConfigs")
    public List<CashPointConfig> getCashpointConfigs() {
        return configModel.getCashpointConfigs();
    }

    @PutMapping("/cashpointConfigs")
    public void updateCashpointConfigs(@RequestBody List<CashPointConfig> cashpointConfigs) {
        configModel.setCashpointConfigs(cashpointConfigs);
    }

    @GetMapping("/entranceCount")
    public int getEntranceCount() {
        return configModel.getEntranceCount();
    }

    @PutMapping("/entranceCount")
    public void updateEntranceCount(@RequestParam int entranceCount) {
        configModel.setEntranceCount(entranceCount);
    }

    @GetMapping("/entranceConfigs")
    public List<EntranceConfig> getEntranceConfigs() {
        return configModel.getEntranceConfigs();
    }

    @PutMapping("/entranceConfigs")
    public void updateEntranceConfigs(@RequestBody List<EntranceConfig> entranceConfigs) {
        configModel.setEntranceConfigs(entranceConfigs);
    }

    @GetMapping("/minServiceTime")
    public int getMinServiceTime() {
        return configModel.getMinServiceTime();
    }

    @PutMapping("/minServiceTime")
    public void updateMinServiceTime(@RequestParam int minServiceTime) {
        configModel.setMinServiceTime(minServiceTime);
    }

    @GetMapping("/maxServiceTime")
    public int getMaxServiceTime() {
        return configModel.getMaxServiceTime();
    }

    @PutMapping("/maxServiceTime")
    public void updateMaxServiceTime(@RequestParam int maxServiceTime) {
        configModel.setMaxServiceTime(maxServiceTime);
    }

    @GetMapping("/maxPeopleAllowed")
    public int getMaxPeopleAllowed() {
        return configModel.getMaxPeopleAllowed();
    }

    @PutMapping("/maxPeopleAllowed")
    public void updateMaxPeopleAllowed(@RequestParam int maxPeopleAllowed) {
        configModel.setMaxPeopleAllowed(maxPeopleAllowed);
    }
}