package iot.tetracubered.resources.actions.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class DeviceActionResponse {

    @JsonProperty("id")
    private final UUID id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("value")
    private final Float value;

    public DeviceActionResponse(UUID id,
                                String name,
                                Float value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Float getValue() {
        return value;
    }
}
