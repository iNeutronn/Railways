package com.railways.railways;

import com.railways.railways.Configuration.*;
import com.railways.railways.domain.station.Direction;
import com.railways.railways.logging.FileLogger;
import com.railways.railways.logging.LogLevel;
import com.railways.railways.logging.Logger;
import com.railways.railways.simulation.RandomPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;
import java.util.List;

@Configuration
public class AppConfig {
    /**
     * Bean for logger: Singleton instance of FileLogger.
     * The logger is used to log messages throughout the application.
     *
     * @return A singleton instance of FileLogger.
     */
    @Bean
    @Scope("singleton")
    public Logger logger() {
        return new FileLogger();
    }

    /**
     * Bean for ConfigModel: Singleton instance of configuration model.
     * Initializes configuration with default values, including random client generation policy, map size, etc.
     *
     * @param logger The logger used to log the configuration creation process.
     * @return A singleton instance of ConfigModel.
     */
    @Bean
    @Scope("singleton")
    public ConfigModel configModel(Logger logger) {
        logger.log("Creating default configuration", LogLevel.Info);

        // Створюємо та повертаємо конфігурацію
        ConfigModel config = new ConfigModel(
                new RandomPolicy(5.0,10.0),
                new MapSize(100,70),
                3, // Кількість касових пунктів
                2, // Кількість входів
                1000, // Мінімальний час обслуговування
                5000, // Максимальний час обслуговування
                50, // Максимальна кількість людей
                10 // швидкість руху клієнтів
        );
        logger.log("Base ConfigModel object created", LogLevel.Debug);

        config.setCashPointSize(5,3);
        logger.log("CashPoint size set to 5x3", LogLevel.Debug);


        EntranceLocationGenerator entranceLocationGenerator = new EntranceLocationGenerator(
                config.getMapSize(), 5, 1);
        CashPointLocationGenerator cashPointLocationGenerator = new CashPointLocationGenerator(
                config.getMapSize(), 5, 3);
        var entranceConfigs = entranceLocationGenerator.getLocations(config.getEntranceCount(), false);

        logger.log("Generated and set entrance configurations: " + entranceConfigs.size(), LogLevel.Debug);

        var cashPointConfigs = cashPointLocationGenerator.getLocations(config.getCashPointCount()+1, false);
        config.setEntranceConfigs(entranceConfigs);
        logger.log("Generated cash point configurations: " + cashPointConfigs.size(), LogLevel.Debug);

        //set all cash point without last one (last is reserved)
        config.setCashpointConfigs(cashPointConfigs.subList(0, cashPointConfigs.size() - 1));
        config.setReservCashPointConfig(cashPointConfigs.getLast());


        logger.log("Configuration model successfully created", LogLevel.Info);
        return config;
    }


}
