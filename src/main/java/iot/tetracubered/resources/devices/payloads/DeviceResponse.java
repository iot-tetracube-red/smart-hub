package iot.tetracubered.resources.devices.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class DeviceResponse {

    @JsonProperty("id")
    private final UUID id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("online")
    private final Boolean online;

    @JsonProperty("colorCode")
    private final String colorCode;

    public DeviceResponse(UUID id,
                          String name,
                          Boolean online,
                          String colorCode) {
        this.id = id;
        this.name = name;
        this.online = online;
        this.colorCode = colorCode;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getOnline() {
        return online;
    }

    public String getColorCode() {
        return colorCode;
    }
}
