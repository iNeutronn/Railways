package com.railways.railways;
import com.railways.railways.Configuration.CashPointConfig;
import com.railways.railways.Configuration.CashPointLocationGenerator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class CashPointLocationGeneratorEdgeCasesTest {

	@Test
	void shouldHandleBoundaryCasesForLocationGeneration() {
		CashPointLocationGenerator generator = new CashPointLocationGenerator(10, 10, 1, 1);
		List<CashPointConfig> locations = generator.getLocations(100, false);

		assertTrue(locations.size() <= 100, "The number of generated locations should not exceed the request.");
		assertTrue(locations.stream().allMatch(location -> location.x >= 0 && location.y >= 0), "Locations should be within boundaries.");
	}

	@Test
	void shouldReturnEmptyListWhenRequestedZeroLocations() {
		CashPointLocationGenerator generator = new CashPointLocationGenerator(100, 100, 10, 10);
		List<CashPointConfig> locations = generator.getLocations(0, false);

		assertTrue(locations.isEmpty(), "Requesting 0 locations should return an empty list.");
	}
}

