package com.railways.railways.Configuration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ConfigController provides REST API to access the configuration of the railway ticketing system.
 */
@RestController
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
    @GetMapping("/config")
    public ConfigModel getConfig() {
        return configModel;
    }
}
