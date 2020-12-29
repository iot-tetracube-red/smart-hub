package iot.tetracubered.devices.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class ActionProvisioningRequest {

    private final UUID id;
    private final String name;
    private final String url;

    @JsonCreator
    public ActionProvisioningRequest(@JsonProperty("id") UUID id,
                                     @JsonProperty("default_name") String name,
                                     @JsonProperty("url") String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
