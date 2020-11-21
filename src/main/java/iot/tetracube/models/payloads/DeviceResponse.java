package iot.tetracube.models.payloads;

import java.util.List;
import java.util.UUID;

public class DeviceResponse {

    private final UUID id;
    private final String name;
    private final List<DeviceResponseAction> actions;

    public DeviceResponse(UUID id, String name, List<DeviceResponseAction> actions) {
        this.id = id;
        this.name = name;
        this.actions = actions;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<DeviceResponseAction> getActions() {
        return actions;
    }

}
