package com.railways.railways;

import com.railways.railways.Configuration.CashPointConfig;
import com.railways.railways.Configuration.CashPointLocationGenerator;
import com.railways.railways.Configuration.ConfigModel;
import com.railways.railways.Configuration.EntranceConfig;
import com.railways.railways.domain.station.Direction;
import com.railways.railways.simulation.GenerationPolicy;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CashPointLocationGeneratorTest {



    @Test
    void testRandomLocationsCount() {
        CashPointLocationGenerator generator = new CashPointLocationGenerator(10, 10, 2, 2);
        List<CashPointConfig> locations = generator.getLocations(3, true);

        assertEquals(3, locations.size());
    }

    @Test
    void testInvalidMapDimensions() {
        assertThrows(IllegalArgumentException.class, () -> new CashPointLocationGenerator(0, 10, 2, 2));
        assertThrows(IllegalArgumentException.class, () -> new CashPointLocationGenerator(10, 0, 2, 2));
    }

    @Test
    void testInvalidCashPointDimensions() {
        assertThrows(IllegalArgumentException.class, () -> new CashPointLocationGenerator(10, 10, 0, 2));
        assertThrows(IllegalArgumentException.class, () -> new CashPointLocationGenerator(10, 10, 2, 0));
    }

    @Test
    void testGetLocationsExceedingCount() {
        CashPointLocationGenerator generator = new CashPointLocationGenerator(10, 10, 2, 2);
        assertThrows(IllegalArgumentException.class, () -> generator.getLocations(10, false));
    }
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
