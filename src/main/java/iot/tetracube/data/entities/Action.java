package iot.tetracube.data.entities;

import io.vertx.mutiny.sqlclient.Row;

import java.util.UUID;

public class Action {

    private UUID id;
    private UUID actionId;
    private String name;
    private boolean isDefault;
    private String publishTopic;
    private String alexaIntent;
    private UUID deviceId;

    public Action(UUID id,
                  UUID actionId,
                  String name,
                  boolean isDefault,
                  String publishTopic,
                  String alexaIntent,
                  UUID deviceId) {
        this.id = id;
        this.actionId = actionId;
        this.name = name;
        this.isDefault = isDefault;
        this.publishTopic = publishTopic;
        this.alexaIntent = alexaIntent;
        this.deviceId = deviceId;
    }

    public Action(Row row) {
        this.id = row.getUUID("id");
        this.actionId = row.getUUID("action_id");
        this.name = row.getString("name");
        this.isDefault = row.getBoolean("is_default");
        this.publishTopic = row.getString("publish_topic");
        this.alexaIntent = row.getString("alexa_intent");
        this.deviceId = row.getUUID("device_id");
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

    public boolean isDefault() {
        return isDefault;
    }

    public String getPublishTopic() {
        return publishTopic;
    }

    public String getAlexaIntent() {
        return alexaIntent;
    }

    public UUID getDeviceId() {
        return deviceId;
    }
}