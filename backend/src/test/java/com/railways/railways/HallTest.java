package com.railways.railways;
import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.client.PrivilegeEnum;
import com.railways.railways.domain.station.Entrance;
import com.railways.railways.domain.station.Hall;
import com.railways.railways.domain.station.Segment;
import com.railways.railways.domain.station.TicketOffice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HallTest {
    private Hall hall;
    private TicketOffice ticketOffice;
    private Entrance entrance;

    @BeforeEach
    public void setup() {
        hall = Hall.getInstance();

        Segment segment = new Segment(new Point(0, 0), new Point(10, 10));
        entrance = new Entrance(1, segment);
        ticketOffice = Mockito.mock(TicketOffice.class);

        List<Entrance> entrances = new ArrayList<>();
        entrances.add(entrance);
        hall.setEntrances(entrances);

        List<TicketOffice> ticketOffices = new ArrayList<>();
        ticketOffices.add(ticketOffice);
        hall.setTicketOffices(ticketOffices);

        Mockito.when(ticketOffice.isOpen()).thenReturn(true);
        Mockito.when(ticketOffice.getSegment()).thenReturn(segment);
        Mockito.when(ticketOffice.getQueueSize()).thenReturn(0);
    }

    @Test
    public void testGetBestTicketOffice() {
        TicketOffice bestOffice = hall.getBestTicketOffice(new Point(0, 0));
        assertNotNull(bestOffice);
    }


}
