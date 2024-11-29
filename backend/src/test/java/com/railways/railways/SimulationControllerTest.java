package com.railways.railways;
import com.railways.railways.controllers.SimulationController;
import com.railways.railways.simulation.SimulationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SimulationController.class)
class SimulationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SimulationService simulationService;

    @BeforeEach
    void setUp() {
        Mockito.reset(simulationService);
    }

    @Test
    void testStartSimulation() throws Exception {
        mockMvc.perform(get("/api/simulation/start"))
                .andExpect(status().isOk())
                .andExpect(content().string("Simulation started!"));

        verify(simulationService).startSimulation();
    }

    @Test
    void testStopSimulation() throws Exception {
        mockMvc.perform(get("/api/simulation/stop"))
                .andExpect(status().isOk())
                .andExpect(content().string("Simulation stopped!"));

        verify(simulationService).stopSimulation();
    }

    @Test
    void testResumeSimulation() throws Exception {
        mockMvc.perform(get("/api/simulation/resume"))
                .andExpect(status().isOk())
                .andExpect(content().string("Simulation resumed!"));

        verify(simulationService).resumeSimulation();
    }

    @Test
    void testCloseCashPoint() throws Exception {
        int id = 1;
        when(simulationService.closeCashPoint(id)).thenReturn(id);

        mockMvc.perform(post("/api/simulation/cashpoint/close")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(id)));

        verify(simulationService).closeCashPoint(id);
    }

    @Test
    void testOpenCashPoint() throws Exception {
        int id = 2;
        when(simulationService.openCashPoint(id)).thenReturn(id);

        mockMvc.perform(post("/api/simulation/cashpoint/open")
                        .param("id", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(id)));

        verify(simulationService).openCashPoint(id);
    }
}
