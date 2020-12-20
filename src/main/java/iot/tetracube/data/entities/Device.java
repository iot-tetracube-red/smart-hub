package iot.tetracube.data.entities;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;

import java.util.List;
import java.util.UUID;

public class Device extends ReactivePanacheMongoEntity {

    public UUID deviceId;
    public String name;
    public Boolean isOnline;
    public String alexaSlotId;
    public String hostName;
    public List<DeviceAction> actions;

    public UUID getDeviceId() {
        return deviceId;
    }

    public String getName() {
        return name;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public String getAlexaSlotId() {
        return alexaSlotId;
    }

    public String getHostName() {
        return hostName;
    }

    public List<DeviceAction> getActions() {
        return actions;
    }
}
