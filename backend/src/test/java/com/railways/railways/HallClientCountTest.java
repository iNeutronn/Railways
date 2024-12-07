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

class HallClientCountTest {

    private Hall hall;
    private TicketOffice ticketOffice;
    private Client client1;
    private Client client2;

    @BeforeEach
    public void setUp() {
        hall = Hall.getInstance();
        ticketOffice = new TicketOffice(null, 1, new Segment(new Point(0, 0), new Point(1, 1)), Direction.NORTH, 5, 10);
        hall.addTicketOffice(ticketOffice);
        client1 = new Client(1, "John", "Doe", 2, new Point(0, 0), PrivilegeEnum.BASIC);
        client2 = new Client(2, "Jane", "Smith", 1, new Point(1, 1), PrivilegeEnum.PREMIUM);
    }

    @Test
    public void testGetClientCount() {
        hall.processClient(client1);
        hall.processClient(client2);

        int clientCount = hall.getClientCount();
        assertEquals(2, clientCount, "There should be 2 clients in the hall.");
    }
}