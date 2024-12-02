package com.railways.railways;

import com.railways.railways.domain.client.ClientGenerator;
import com.railways.railways.domain.station.Hall;
import com.railways.railways.logging.LogLevel;
import com.railways.railways.simulation.HallSimulator;
import com.railways.railways.Configuration.ConfigModel;
import com.railways.railways.logging.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.*;

class HallSimulatorTest {

    @Test
    void testSimulatorStartAndStop() throws InterruptedException {
        // Мок об'єкти
        Hall mockHall = mock(Hall.class);
        ConfigModel mockConfig = mock(ConfigModel.class);
        ClientGenerator mockClientGenerator = mock(ClientGenerator.class);
        ApplicationEventPublisher mockEventPublisher = mock(ApplicationEventPublisher.class);
        Logger mockLogger = mock(Logger.class);

        when(mockConfig.getMaxPeopleAllowed()).thenReturn(10);
        when(mockConfig.getGenerationPolicy().getSeconds()).thenReturn(0.5);

        HallSimulator simulator = new HallSimulator(mockEventPublisher, mockConfig, mockHall, mockClientGenerator, mockLogger);
        Thread thread = new Thread(simulator);
        thread.start();

        // Запуск симуляції
        simulator.start();
        Thread.sleep(1000);

        // Зупинка симуляції
        simulator.stop();
        Thread.sleep(500);

        // Перевірка викликів
        verify(mockLogger).log("Simulation started", LogLevel.Info);
        verify(mockLogger).log("Simulation paused", LogLevel.Warning);

        thread.interrupt();
    }
}
