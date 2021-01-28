package iot.tetracubered.resources.features.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import iot.tetracubered.enumerations.FeatureType;

import java.util.UUID;

public class DeviceFeatureResponse {

    @JsonProperty("id")
    private final UUID id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("type")
    private final FeatureType type;

    @JsonProperty("value")
    private final Float value;

    public DeviceFeatureResponse(UUID id,
                                 String name,
                                 FeatureType type,
                                 Float value) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.type = type;
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

    public FeatureType getType() {
        return type;
    }
}
