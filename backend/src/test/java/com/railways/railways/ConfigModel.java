package com.railways.railways;

import com.railways.railways.Configuration.CashPointConfig;
import com.railways.railways.Configuration.ConfigModel;
import com.railways.railways.Configuration.EntranceConfig;
import com.railways.railways.simulation.GenerationPolicy;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConfigModelTest {

    @Test
    void testValidConfigModelInitialization() {
        // Use Mockito to create a mock of GenerationPolicy
        GenerationPolicy mockPolicy = Mockito.mock(GenerationPolicy.class);

        List<CashPointConfig> cashPoints = List.of(new CashPointConfig(), new CashPointConfig());
        List<EntranceConfig> entrances = List.of(new EntranceConfig(), new EntranceConfig());

        // Initialize ConfigModel with valid parameters
        ConfigModel configModel = new ConfigModel(
                mockPolicy,
                2,
                cashPoints,
                new CashPointConfig(),
                entrances,
                2,
                500,
                1000,
                200,
                1.5
        );

        // Assertions to verify the correctness of the configuration
        assertNotNull(configModel.getGenerationPolicy());
        assertEquals(2, configModel.getCashPointCount());
        assertEquals(2, configModel.getEntranceCount());
        assertEquals(500, configModel.getMinServiceTime());
        assertEquals(1000, configModel.getMaxServiceTime());
        assertEquals(200, configModel.getMaxPeopleAllowed());
        assertEquals(1.5, configModel.getClientSpeed());
    }

    @Test
    void testInvalidCashPointsCount() {
        // Test for invalid cashpoints count (negative value)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ConfigModel(null, -1, Collections.emptyList(), new CashPointConfig(), Collections.emptyList(), 1, 100, 200, 50, 1.0);
        });
        assertEquals("The number of cashpoints must be greater than zero.", exception.getMessage());
    }

    @Test
    void testSetMinServiceTimeThrowsException() {
        // Test for invalid minServiceTime (negative value)
        ConfigModel configModel = new ConfigModel(
                Mockito.mock(GenerationPolicy.class),
                1,
                List.of(new CashPointConfig()),
                new CashPointConfig(),
                List.of(new EntranceConfig()),
                1,
                500,
                1000,
                200,
                1.5
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            configModel.setMinServiceTime(-100);
        });
        assertEquals("Minimum service time must be greater than zero.", exception.getMessage());
    }

    @Test
    void testUpdateConfig() {
        // Initial configuration setup
        ConfigModel initialConfig = new ConfigModel(
                Mockito.mock(GenerationPolicy.class),
                1,
                List.of(new CashPointConfig()),
                new CashPointConfig(),
                List.of(new EntranceConfig()),
                1,
                500,
                1000,
                200,
                1.5
        );

        // New updated configuration setup
        ConfigModel updatedConfig = new ConfigModel(
                Mockito.mock(GenerationPolicy.class),
                2,
                List.of(new CashPointConfig(), new CashPointConfig()),
                new CashPointConfig(),
                List.of(new EntranceConfig(), new EntranceConfig()),
                2,
                700,
                1200,
                300,
                2.0
        );

        // Update initialConfig with the updatedConfig values
        initialConfig.updateConfig(updatedConfig);

        // Assertions to verify that the configuration is updated correctly
        assertEquals(2, initialConfig.getCashPointCount());
        assertEquals(700, initialConfig.getMinServiceTime());
        assertEquals(1200, initialConfig.getMaxServiceTime());
        assertEquals(300, initialConfig.getMaxPeopleAllowed());
    }
}
