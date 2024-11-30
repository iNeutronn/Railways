package com.railways.railways;

import com.railways.railways.domain.client.Client;
import com.railways.railways.domain.client.ClientCreated;
import com.railways.railways.domain.client.PrivilegeEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientCreatedTest {

    @Test
    void testClientCreatedInitialization() {
        Client client = new Client(1, "John", "Doe", 2, PrivilegeEnum.DISABLED);
        ClientCreated clientCreated = new ClientCreated();

        assertNotNull(clientCreated);
    }
}
