package com.railways.railways;
import com.railways.railways.Configuration.EntranceConfig;
import com.railways.railways.Configuration.EntranceLocationGenerator;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
class EntranceLocationGeneratorTest {

    @Test
    void testValidLocationGeneration() {
        EntranceLocationGenerator generator = new EntranceLocationGenerator(100, 50, 10, 5);

        List<EntranceConfig> locations = generator.getLocations(3, false);
        assertEquals(3, locations.size());
    }

    @Test
    void testInvalidMapDimensions() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new EntranceLocationGenerator(-100, 50, 10, 5);
        });
        assertEquals("Map dimensions must be greater than zero.", exception.getMessage());
    }

    @Test
    void testInvalidEntranceDimensions() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new EntranceLocationGenerator(100, 50, 110, 5);
        });
        assertEquals("Entrance dimensions must fit within the map dimensions.", exception.getMessage());
    }

    @Test
    void testGetLocationsThrowsExceptionForInvalidCount() {
        EntranceLocationGenerator generator = new EntranceLocationGenerator(100, 50, 10, 5);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            generator.getLocations(10, false);
        });
        assertEquals("Not enough locations available.", exception.getMessage());
    }

    @Test
    void testRandomOrderLocationGeneration() {
        EntranceLocationGenerator generator = new EntranceLocationGenerator(100, 50, 10, 5);

        List<EntranceConfig> orderedLocations = generator.getLocations(3, false);
        List<EntranceConfig> randomLocations = generator.getLocations(3, true);

        assertEquals(orderedLocations.size(), randomLocations.size());
        // Verify that the random order is different from the ordered list
        assertNotEquals(orderedLocations, randomLocations);
    }
}
