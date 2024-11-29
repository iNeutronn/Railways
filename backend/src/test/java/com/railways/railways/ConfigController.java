package com.railways.railways;

import com.railways.railways.Configuration.ConfigController;
import com.railways.railways.Configuration.ConfigModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ConfigControllerTest {

    @Autowired
    private ConfigController configController;

    @Test
    void testGetConfig() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(configController).build();

        mockMvc.perform(get("/config"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateCashpointsCount() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(configController).build();

        mockMvc.perform(post("/config/cashpointsCount")
                        .param("cashpointsCount", "5"))
                .andExpect(status().isOk());
    }
}
