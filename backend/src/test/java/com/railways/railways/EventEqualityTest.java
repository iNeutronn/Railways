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

class EventEqualityTest {

    @Test
    public void testClientCreatedEventEquality() {
        Client client = new Client(1, "John", "Doe", 2, null, PrivilegeEnum.BASIC);
        ClientCreatedEvent event1 = new ClientCreatedEvent(this, client);
        ClientCreatedEvent event2 = new ClientCreatedEvent(this, client);

        assertEquals(event1, event2, "Two ClientCreatedEvents with the same client should be equal.");
    }

    @Test
    public void testClientServedEventEquality() {
        ServeRecord serveRecord = new ServeRecord(1, 1, 2, "2024-11-24T10:00:00Z", "2024-11-24T10:10:00Z");
        ClientServedEvent event1 = new ClientServedEvent(this, serveRecord);
        ClientServedEvent event2 = new ClientServedEvent(this, serveRecord);

        assertEquals(event1, event2, "Two ClientServedEvents with the same record should be equal.");
    }
}