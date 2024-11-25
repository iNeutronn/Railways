package com.railways.railways;

import com.railways.railways.Configuration.CashPointConfig;
import com.railways.railways.Configuration.ConfigModel;
import com.railways.railways.Configuration.EntranceConfig;
import com.railways.railways.Configuration.MapSize;
import com.railways.railways.domain.station.Direction;
import com.railways.railways.simulation.IntervalPolicy;
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
        // Створюємо дефолтні конфігурації для касових пунктів
        List<CashPointConfig> cashpointConfigs = Arrays.asList(
                new CashPointConfig() {{ id = 1; x = 0; y = 3; direction = Direction.Down; }},
                new CashPointConfig() {{ id = 2; x = 5; y = 3; direction = Direction.Down; }},
                new CashPointConfig() {{ id = 3; x = 10; y = 3; direction = Direction.Down; }}
        );

        // Створюємо дефолтну конфігурацію для резервного касового пункту
        CashPointConfig reservCashPointConfig = new CashPointConfig() {{
            id = 4;
            x = 15;
            y = 3;
            direction = Direction.Down;
        }};

        // Створюємо дефолтні конфігурації для входів
        List<EntranceConfig> entranceConfigs = Arrays.asList(
                new EntranceConfig() {{id = 1; x = 10; y = 10; }},
                new EntranceConfig() {{id = 2; x = 15; y = 15; }}
        );

        // Створюємо та повертаємо конфігурацію
        ConfigModel config = new ConfigModel(
                new RandomPolicy(5.0,10.0),
                new MapSize(100,70),
                3, // Кількість касових пунктів
                cashpointConfigs,
                reservCashPointConfig,
                entranceConfigs,
                2, // Кількість входів
                1000, // Мінімальний час обслуговування
                5000, // Максимальний час обслуговування
                50, // Максимальна кількість людей
                10 // швидкість руху клієнтів
        );


        config.setCashPointSize(5,3);
        return config;
    }
}
