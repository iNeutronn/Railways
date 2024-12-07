package com.railways.railways;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import com.railways.railways.Configuration.CashPointConfig;
import com.railways.railways.Configuration.CashPointLocationGenerator;
import com.railways.railways.Configuration.ConfigModel;
import com.railways.railways.domain.ServeRecord;
import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.client.PrivilegeEnum;
import com.railways.railways.domain.station.Direction;
import com.railways.railways.domain.station.Hall;
import com.railways.railways.domain.station.Segment;
import com.railways.railways.domain.station.TicketOffice;
import com.railways.railways.events.ClientCreatedEvent;
import com.railways.railways.events.ClientMovedEvent;
import com.railways.railways.events.ClientServedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import java.awt.*;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ConfigModelTest {

    @Test
    void shouldThrowExceptionForInvalidCashPointCount() {
        assertThrows(IllegalArgumentException.class, () -> new ConfigModel(null, 0, Collections.emptyList(), null, Collections.emptyList(), 2, 1000, 2000, 50));
    }

    @Test
    void shouldThrowExceptionForInvalidServiceTime() {
        assertThrows(IllegalArgumentException.class, () -> new ConfigModel(null, 5, Collections.emptyList(), null, Collections.emptyList(), 2, -1, 2000, 50));
        assertThrows(IllegalArgumentException.class, () -> new ConfigModel(null, 5, Collections.emptyList(), null, Collections.emptyList(), 2, 1000, 500, 50));
    }

    @Test
    void shouldSetAndRetrieveCashPointConfigs() {
        CashPointConfig config = new CashPointConfig();
        config.x = 10;
        config.y = 20;
        config.direction = Direction.Down;

        ConfigModel model = new ConfigModel(null, 1, Collections.singletonList(config), null, Collections.emptyList(), 1, 1000, 2000, 50);
        assertEquals(1, model.getCashpointConfigs().size());
        assertEquals(10, model.getCashpointConfigs().get(0).x);
        assertEquals(20, model.getCashpointConfigs().get(0).y);
    }
}