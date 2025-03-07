package org.example.app.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Endpoint(id = "uuid")
public class GetUUID {
    @ReadOperation
    public UUID getUUID() {
        return UUID.randomUUID();
    }
}
