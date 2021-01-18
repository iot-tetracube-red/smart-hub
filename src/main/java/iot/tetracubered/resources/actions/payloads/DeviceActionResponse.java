package iot.tetracubered.resources.actions.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class DeviceActionResponse {

    @JsonProperty("id")
    private final UUID id;

    @JsonProperty("name")
    private final String name;

    public DeviceActionResponse(UUID id,
                                String name) {
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
