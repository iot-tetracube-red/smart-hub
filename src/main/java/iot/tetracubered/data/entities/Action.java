package iot.tetracubered.data.entities;

import io.vertx.mutiny.sqlclient.Row;

import java.util.UUID;

public class Action implements BaseEntity {

    private UUID id;
    private UUID actionId;
    private String triggerTopic;
    private String name;

    // External references
    private Feature feature;

    public static Action generateNewAction(UUID actionId,
                                           String triggerTopic,
                                           String name,
                                           Feature feature) {
        var action = new Action();
        action.id = UUID.randomUUID();
        action.actionId = actionId;
        action.triggerTopic = triggerTopic;
        action.name = name;
        action.feature = feature;
        return action;
    }

    @Override
    public void populateFromRow(Row row) {
        this.id = row.getUUID("id");
        this.actionId = row.getUUID("action_id");
        this.triggerTopic = row.getString("trigger_topic");
        this.name = row.getString("name");
    }

    public UUID getActionId() {
        return actionId;
    }

    public Feature getFeature() {
        return feature;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTriggerTopic() {
        return triggerTopic;
    }

    public void setActionId(UUID actionId) {
        this.actionId = actionId;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTriggerTopic(String triggerTopic) {
        this.triggerTopic = triggerTopic;
    }
}
