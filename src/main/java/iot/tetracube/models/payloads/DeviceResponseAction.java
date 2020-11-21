package iot.tetracube.models.payloads;

import java.util.UUID;

public class DeviceResponseAction {

    private final UUID id;
    private final String name;

    public DeviceResponseAction(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
