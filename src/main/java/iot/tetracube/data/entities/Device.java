package iot.tetracube.data.entities;

import io.vertx.mutiny.sqlclient.Row;

import java.util.UUID;

public class Device {

    private UUID id;
    private UUID circuitId;
    private String name;
    private Boolean isOnline;
    private String alexaSlotId;
    private String clientName;

    public Device(UUID id,
                  UUID circuitId,
                  String name,
                  Boolean isOnline,
                  String clientName) {
        this.id = id;
        this.circuitId = circuitId;
        this.name = name;
        this.isOnline = isOnline;
        this.clientName = clientName;
    }

    public Device(Row row) {
        this.id = row.getUUID("id");
        this.circuitId = row.getUUID("circuit_id");
        this.name = row.getString("name");
        this.isOnline = row.getBoolean("is_online");
        this.alexaSlotId = row.getString("alexa_slot_id");
        this.clientName = row.getString("client_name");
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(UUID circuitId) {
        this.circuitId = circuitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    public String getAlexaSlotId() {
        return alexaSlotId;
    }

    public void setAlexaSlotId(String alexaSlotId) {
        this.alexaSlotId = alexaSlotId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

}
