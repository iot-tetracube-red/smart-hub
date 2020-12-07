package iot.tetracube.data.entities;

import io.vertx.mutiny.sqlclient.Row;

import java.util.List;
import java.util.UUID;

public class Device {

    private UUID id;
    private UUID circuitId;
    private String name;
    private Boolean isOnline;
    private String alexaSlotId;
    private String clientName;
    private List<Action> actions;

    public Device(Row row) {
        this.id = row.getUUID("id");
        this.circuitId = row.getUUID("circuit_id");
        this.name = row.getString("name");
        this.isOnline = row.getBoolean("is_online");
        this.alexaSlotId = row.getString("alexa_slot_id");
        this.clientName = row.getString("client_name");
    }

    public Device(UUID id, UUID circuitId, String name, Boolean isOnline, String clientName) {
        this.id = id;
        this.circuitId = circuitId;
        this.name = name;
        this.isOnline = isOnline;
        this.clientName = clientName;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCircuitId() {
        return circuitId;
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

    public String getClientName() {
        return clientName;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}
