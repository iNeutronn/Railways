package com.railways.railways.communication.DTO;

import com.railways.railways.domain.ServeRecord;
import com.railways.railways.domain.client.ClientRedirected;

public class ClientRedirectedUpdate extends GenerationUpdateDTO {
    public ClientRedirectedUpdate(ClientRedirected redirected) {
        super(GenerationUpdateTypes.ClientRedirected, redirected);
    }
}
