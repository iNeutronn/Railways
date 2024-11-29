package com.railways.railways;

import com.railways.railways.Configuration.ConfigModel;
import com.railways.railways.domain.ClientMovementHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientMovementHelperTest {

    private ClientMovementHelper clientMovementHelper;
    private static final double DELTA = 0.001;

    @BeforeEach
    void setUp() {
        // Mock the ConfigModel
        ConfigModel mockConfig = Mockito.mock(ConfigModel.class);
        Mockito.when(mockConfig.getClientSpeed()).thenReturn(10.0);

        clientMovementHelper = new ClientMovementHelper(mockConfig);
    }

    @Test
    void testGetDistance_SamePoint() {
        double distance = ClientMovementHelper.getDistance(0, 0, 0, 0);
        assertEquals(0.0, distance, DELTA);
    }

    @Test
    void testGetDistance_DifferentPoints() {
        double distance = ClientMovementHelper.getDistance(0, 0, 3, 4);
        assertEquals(5.0, distance, DELTA);
    }

    @Test
    void testGetDistance_NegativeCoordinates() {
        double distance = ClientMovementHelper.getDistance(-3, -4, 0, 0);
        assertEquals(5.0, distance, DELTA);
    }

    @Test
    void testGetMovementTime_SamePoint() {
        double time = clientMovementHelper.getMovementTime(0, 0, 0, 0);
        assertEquals(0.0, time, DELTA);
    }

    @Test
    void testGetMovementTime_DifferentPoints() {
        double time = clientMovementHelper.getMovementTime(0, 0, 3, 4);
        assertEquals(0.5, time, DELTA);
    }

    @Test
    void testGetMovementTime_NegativeCoordinates() {
        double time = clientMovementHelper.getMovementTime(-3, -4, 0, 0);
        assertEquals(0.5, time, DELTA);
    }
}
