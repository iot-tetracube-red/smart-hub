package iot.tetracubered.data.entities;

import io.vertx.mutiny.sqlclient.Row;

import java.util.UUID;

public class Action {

    private final UUID id;
    private final UUID actionId;
    private final String name;
    private final String commandTopic;
    private final String alexaIntent;
    private final UUID deviceId;
    private final Float value;

    public Action(Row row) {
        this.id = row.getUUID("id");
        this.actionId = row.getUUID("action_id");
        this.name = row.getString("name");
        this.commandTopic = row.getString("command_topic");
        this.alexaIntent = row.getString("alexa_intent");
        this.deviceId = row.getUUID("device_id");
        this.value = row.getFloat("value");
    }

    public UUID getId() {
        return id;
    }

    public UUID getActionId() {
        return actionId;
    }

    public String getName() {
        return name;
    }

    public String getCommandTopic() {
        return commandTopic;
    }

    public String getAlexaIntent() {
        return alexaIntent;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public Float getValue() {
        return value;
    }
}
