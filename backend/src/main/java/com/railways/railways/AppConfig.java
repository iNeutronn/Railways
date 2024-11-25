package com.railways.railways;

import com.railways.railways.Configuration.*;
import com.railways.railways.domain.station.Direction;
import com.railways.railways.logging.Logger;
import com.railways.railways.logging.ConsoleLogger;
import com.railways.railways.simulation.RandomPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;
import java.util.List;

@Configuration
public class AppConfig {
    @Bean
    @Scope("singleton")
    public ConfigModel configModel() {
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


        config.setCashPointSize(5,3);


        EntranceLocationGenerator entranceLocationGenerator = new EntranceLocationGenerator(
                config.getMapSize(), 5, 1);
        CashPointLocationGenerator cashPointLocationGenerator = new CashPointLocationGenerator(
                config.getMapSize(), 5, 3);
        var entranceConfigs = entranceLocationGenerator.getLocations(config.getEntranceCount(), false);
        var cashPointConfigs = cashPointLocationGenerator.getLocations(config.getCashPointCount()+1, false);
        config.setEntranceConfigs(entranceConfigs);
        //set all cash point without last one (last is reserved)
        config.setCashpointConfigs(cashPointConfigs.subList(0, cashPointConfigs.size() - 1));
        config.setReservCashPointConfig(cashPointConfigs.getLast());


        return config;
    }

    @Bean
    @Scope("singleton")
    public Logger logger()
    {
        return  new ConsoleLogger();
    }
}
