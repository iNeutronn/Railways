package com.railways.railways;

import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.client.ClientGenerator;
import com.railways.railways.domain.client.PrivilegeEnum;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ClientGeneratorTest {

    @Test
    void testGenerateClient() {
        Map<PrivilegeEnum, Integer> weights = new EnumMap<>(PrivilegeEnum.class);
        weights.put(PrivilegeEnum.DISABLED, 1);
        weights.put(PrivilegeEnum.WITHCHILD, 2);
        weights.put(PrivilegeEnum.WARVETERAN, 1);
        weights.put(PrivilegeEnum.DEFAULT, 3);

        ClientGenerator generator = new ClientGenerator(weights);
        Client client = generator.generateClient(1001);

        assertNotNull(client);
        assertTrue(client.getClientID() > 0);
        assertNotNull(client.getFirstName());
        assertNotNull(client.getLastName());
        assertTrue(client.getTicketsToBuy() > 0 && client.getTicketsToBuy() <= 5);
        assertNotNull(client.getPrivilege());
    }

    @Test
    void testWeightedRandomPrivilege() {
        Map<PrivilegeEnum, Integer> weights = new EnumMap<>(PrivilegeEnum.class);
        weights.put(PrivilegeEnum.DISABLED, 1);
        weights.put(PrivilegeEnum.WITHCHILD, 1);

        ClientGenerator generator = new ClientGenerator(weights);
        PrivilegeEnum privilege = generator.generateClient(1).getPrivilege();

        assertTrue(weights.containsKey(privilege));
    }
}
