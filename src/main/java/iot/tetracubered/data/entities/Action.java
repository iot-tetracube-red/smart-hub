package iot.tetracubered.data.entities;

import io.vertx.mutiny.sqlclient.Row;

import java.util.UUID;

public class Action {

    private final UUID id;
    private final UUID actionId;
    private final String triggerTopic;
    private final String name;
    private final UUID featureId;

    public Action(UUID id,
                  UUID actionId,
                  String triggerTopic,
                  String name,
                  UUID featureId) {
        this.id = id;
        this.actionId = actionId;
        this.triggerTopic = triggerTopic;
        this.name = name;
        this.featureId = featureId;
    }

    public Action(Row row) {
        this.id = row.getUUID("id");
        this.actionId = row.getUUID("action_id");
        this.name = row.getString("name");
        this.triggerTopic = row.getString("trigger_topic");
        this.featureId = row.getUUID("feature_id");
    }

    public UUID getId() {
        return id;
    }

    public UUID getActionId() {
        return actionId;
    }

    public String getTriggerTopic() {
        return triggerTopic;
    }

    public String getName() {
        return name;
    }

    public UUID getFeatureId() {
        return featureId;
    }
}
