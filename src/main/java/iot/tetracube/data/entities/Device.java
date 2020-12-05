package iot.tetracube.data.entities;

import org.neo4j.driver.Record;

import java.util.UUID;

public class Device {

    private UUID id;
    private UUID circuitId;
    private String name;
    private Boolean isOnline;
    private String alexaSlotId;
    private String clientName;

    public Device(Record record) {
        if(record.size() == 0) {
            return;
        }
        var node = record.get(0).asNode();
        this.id = UUID.fromString(node.get("id").asString());
        this.circuitId = UUID.fromString(node.get("circuitId").asString());
        this.name = node.get("name").asString();
        this.isOnline = node.get("isOnline").asBoolean();
        this.alexaSlotId = node.get("alexaSlotId").asString();
        this.clientName = node.get("clientName").asString();
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
}
