package iot.tetracubered.devices.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class DeviceProvisioningRequest {

    private final UUID id;
    private final String name;
    private final String hostIp;
    private final String hostName;
    private final List<ActionProvisioningRequest> actions;

    @JsonCreator
    public DeviceProvisioningRequest(@JsonProperty("id") UUID id,
                                     @JsonProperty("default_name") String name,
                                     @JsonProperty("host_ip") String hostIp,
                                     @JsonProperty("hostname") String hostName,
                                     @JsonProperty("actions") List<ActionProvisioningRequest> actions) {
        this.id = id;
        this.name = name;
        this.hostIp = hostIp;
        this.hostName = hostName;
        this.actions = actions;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHostIp() {
        return hostIp;
    }

    public String getHostName() {
        return hostName;
    }
}
