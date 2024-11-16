package com.railways.railways;

import com.railways.railways.Configuration.ConfigModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class AppConfig {

    @Bean
    public ConfigModel configModel() {
        return new ConfigModel(
                3,
                Arrays.asList(new int[]{0, 0}, new int[]{1, 1}, new int[]{2, 2}),
                2,
                1000,
                5000,
                50
        );
    }
}
