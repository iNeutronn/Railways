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
class CashPointLocationGeneratorTest {

    @Test
    void shouldThrowExceptionForInvalidMapSize() {
        assertThrows(IllegalArgumentException.class, () -> new CashPointLocationGenerator(-1, 100, 10, 10));
        assertThrows(IllegalArgumentException.class, () -> new CashPointLocationGenerator(100, -1, 10, 10));
    }

    @Test
    void shouldThrowExceptionForInvalidCashPointSize() {
        assertThrows(IllegalArgumentException.class, () -> new CashPointLocationGenerator(100, 100, -10, 10));
        assertThrows(IllegalArgumentException.class, () -> new CashPointLocationGenerator(100, 100, 10, -10));
    }

    @Test
    void shouldThrowExceptionForTooLargeCashPointSize() {
        assertThrows(IllegalArgumentException.class, () -> new CashPointLocationGenerator(10, 10, 10, 11));
    }

    @Test
    void shouldReturnRequestedNumberOfLocations() {
        CashPointLocationGenerator generator = new CashPointLocationGenerator(100, 100, 10, 10);
        List<CashPointConfig> locations = generator.getLocations(5, false);
        assertEquals(5, locations.size(), "Expected 5 locations.");
    }

    @Test
    void shouldShuffleLocationsWhenRandomOrderIsTrue() {
        CashPointLocationGenerator generator = new CashPointLocationGenerator(100, 100, 10, 10);
        List<CashPointConfig> ordered = generator.getLocations(5, false);
        List<CashPointConfig> shuffled = generator.getLocations(5, true);
        assertNotEquals(ordered, shuffled, "Expected shuffled order to differ from ordered.");
    }
}

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

class ConfigControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConfigModel configModel;

    @Test
    void shouldReturnConfig() throws Exception {
        when(configModel.getCashPointCount()).thenReturn(5);

        mockMvc.perform((org.springframework.test.web.servlet.RequestBuilder) get("/config"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.cashpointsCount").value(5));
    }

    private RequestBuilder get(String s) {
        return null;
    }

    @Test
    void shouldReturnCashPointCount() throws Exception {
        when(configModel.getCashPointCount()).thenReturn(3);

        mockMvc.perform(get("/config/cashpointsCount"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content().string("3"));
    }
}

class HallTest {

    private Hall hall;
    private TicketOffice ticketOffice;

    @BeforeEach
    public void setUp() {
        hall = Hall.getInstance();
        ticketOffice = new TicketOffice(null, 1, new Segment(new Point(0, 0), new Point(1, 1)), Direction.NORTH, 5, 10);
    }

    @Test
    public void testAddTicketOffice() {
        hall.addTicketOffice(ticketOffice);
        assertEquals(1, hall.getTicketOffices().size(), "The ticket office should be added to the hall.");
    }
}

class HallProcessClientTest {

    private Hall hall;
    private TicketOffice ticketOffice;
    private Client client;

    @BeforeEach
    public void setUp() {
        hall = Hall.getInstance();
        ticketOffice = new TicketOffice(null, 1, new Segment(new Point(0, 0), new Point(1, 1)), Direction.NORTH, 5, 10);
        hall.addTicketOffice(ticketOffice);
        client = new Client(1, "John", "Doe", 2, new Point(0, 0), PrivilegeEnum.BASIC);
    }

    @Test
    public void testProcessClient() {
        hall.processClient(client);
        assertEquals(client.getPosition(), ticketOffice.getSegment().start, "Client should be assigned to the correct position.");
        assertEquals(1, ticketOffice.getQueueSize(), "The ticket office should have 1 client in the queue.");
    }
}

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

class HallBestTicketOfficeTest {

    private Hall hall;
    private TicketOffice ticketOffice1;
    private TicketOffice ticketOffice2;
    private Client client;

    @BeforeEach
    public void setUp() {
        hall = Hall.getInstance();
        ticketOffice1 = new TicketOffice(null, 1, new Segment(new Point(0, 0), new Point(1, 1)), Direction.NORTH, 5, 10);
        ticketOffice2 = new TicketOffice(null, 2, new Segment(new Point(10, 10), new Point(11, 11)), Direction.SOUTH, 5, 10);
        hall.addTicketOffice(ticketOffice1);
        hall.addTicketOffice(ticketOffice2);
        client = new Client(1, "John", "Doe", 2, new Point(0, 0), PrivilegeEnum.BASIC);
    }

    @Test
    public void testGetBestTicketOffice() {
        hall.processClient(client);
        TicketOffice bestOffice = hall.getBestTicketOffice(client);
        assertNotNull(bestOffice, "The best ticket office should be selected.");
        assertEquals(ticketOffice1.getOfficeID(), bestOffice.getOfficeID(), "Ticket office 1 should be selected as the best office.");
    }
}

class ClientCreatedEventTest {

    @Test
    public void testClientCreatedEvent() {
        // Створюємо клієнта
        Client client = new Client(1, "John", "Doe", 2, null, PrivilegeEnum.BASIC);

        // Створюємо подію
        ClientCreatedEvent event = new ClientCreatedEvent(this, client);

        // Перевіряємо, чи правильно повертається клієнт з події
        assertEquals(client, event.getClient(), "The client should be correctly associated with the event.");
    }
}

class ClientMovedEventTest {

    @Test
    public void testClientMovedEvent() {
        // Створюємо клієнта
        Client client = new Client(1, "John", "Doe", 2, null, PrivilegeEnum.BASIC);

        // Створюємо подію
        ClientMovedEvent event = new ClientMovedEvent(this, client);

        // Перевіряємо, чи правильно повертається клієнт з події
        assertEquals(client, event.getClient(), "The client should be correctly associated with the event.");
    }
}

class ClientServedEventTest {

    @Test
    public void testClientServedEvent() {
        // Створюємо ServeRecord (запис про обслуговування)
        ServeRecord serveRecord = new ServeRecord(1, 1, 2, "2024-11-24T10:00:00Z", "2024-11-24T10:10:00Z");

        // Створюємо подію
        ClientServedEvent event = new ClientServedEvent(this, serveRecord);

        // Перевіряємо, чи правильно повертається ServeRecord з події
        assertEquals(serveRecord, event.getServeRecord(), "The serve record should be correctly associated with the event.");
    }
}

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

class ConfigControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConfigModel configModel;

    @Test
    void shouldReturnConfigModelDetails() throws Exception {
        when(configModel.getCashPointCount()).thenReturn(4);

        mockMvc.perform(get("/config"))

                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.cashpointsCount").value(4))
                .andExpect((ResultMatcher) jsonPath("$.serviceTime").isNotEmpty());
    }

    @Test
    void shouldHandleInvalidEndpointGracefully() throws Exception {
        mockMvc.perform(get("/invalidEndpoint"))
                .andExpect(status().isNotFound());
    }
}
