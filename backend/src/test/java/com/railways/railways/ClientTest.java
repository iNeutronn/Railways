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

class ClientTest {

    @Test
    public void testClientInitialization() {
        Client client = new Client(1, "John", "Doe", 2, new Point(5, 5), PrivilegeEnum.BASIC);
        assertEquals(1, client.getId(), "Client ID should be initialized correctly.");
        assertEquals("John", client.getFirstName(), "Client first name should be initialized correctly.");
        assertEquals("Doe", client.getLastName(), "Client last name should be initialized correctly.");
        assertEquals(new Point(5, 5), client.getPosition(), "Client position should be initialized correctly.");
        assertEquals(PrivilegeEnum.BASIC, client.getPrivilege(), "Client privilege should be initialized correctly.");
    }

    @Test
    public void testUpdatePosition() {
        Client client = new Client(1, "John", "Doe", 2, new Point(0, 0), PrivilegeEnum.BASIC);
        Point newPosition = new Point(10, 10);
        client.setPosition(newPosition);
        assertEquals(newPosition, client.getPosition(), "Client position should be updated.");
    }
}