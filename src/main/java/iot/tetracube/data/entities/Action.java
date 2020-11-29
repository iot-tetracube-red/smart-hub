package iot.tetracube.data.entities;

import io.vertx.mutiny.sqlclient.Row;

import java.util.UUID;

public class Action {

    private UUID id;
    private UUID deviceId;
    private String topic;
    private String name;
    private UUID hardwareId;
    private String alexaIntent;

    public Action(UUID id,
                  UUID deviceId,
                  String topic,
                  String name,
                  UUID hardwareId) {
        this.id = id;
        this.deviceId = deviceId;
        this.name = name;
        this.hardwareId = hardwareId;
        this.topic = topic;
    }

    public Action(Row row) {
        this.id = row.getUUID("id");
        this.deviceId = row.getUUID("device_id");
        this.topic = row.getString("topic");
        this.name = row.getString("name");
        this.hardwareId = row.getUUID("hardware_id");
        this.alexaIntent = row.getString("alexa_intent");
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(UUID hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getAlexaIntent() {
        return alexaIntent;
    }

    public void setAlexaIntent(String alexaIntent) {
        this.alexaIntent = alexaIntent;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

}
