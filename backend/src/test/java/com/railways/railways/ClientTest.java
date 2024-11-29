package com.railways.railways;
import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.client.PrivilegeEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void testClientInitialization() {
        Client client = new Client(1, "John", "Doe", 3, PrivilegeEnum.DISABLED);

        assertEquals(1, client.getClientID());
        assertEquals("John", client.getFirstName());
        assertEquals("Doe", client.getLastName());
        assertEquals("John Doe", client.getFullName());
        assertEquals(3, client.getTicketsToBuy());
        assertEquals(PrivilegeEnum.DISABLED, client.getPrivilege());
        assertTrue(client.getTimestamp() > 0);
    }

    @Test
    void testSetPrivilege() {
        Client client = new Client(2, "Jane", "Smith", 2, PrivilegeEnum.DEFAULT);
        client.setPrivilege(PrivilegeEnum.WARVETERAN);

        assertEquals(PrivilegeEnum.WARVETERAN, client.getPrivilege());
    }

    @Test
    void testSetTicketsToBuy() {
        Client client = new Client(3, "Alice", "Taylor", 1, PrivilegeEnum.WITHCHILD);
        client.setTicketsToBuy(5);

        assertEquals(5, client.getTicketsToBuy());
    }

    @Test
    void testGetPriority() {
        Client client = new Client(4, "Bob", "Brown", 2, PrivilegeEnum.WITHCHILD);
        assertEquals(3, client.getPriority());
    }
}
