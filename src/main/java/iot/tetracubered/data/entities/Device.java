package iot.tetracubered.data.entities;

import io.vertx.mutiny.sqlclient.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Device {

    private final UUID id;
    private final UUID circuitId;
    private final String name;
    private final Boolean isOnline;
    private final String feedbackTopic;
    private final String queryingTopic;
    private final String alexaSlotId;
    private final String colorCode;
    private final List<Action> actions;

    public Device(Row row) {
        this.id = row.getUUID("id");
        this.circuitId = row.getUUID("circuit_id");
        this.name = row.getString("name");
        this.isOnline = row.getBoolean("is_online");
        this.feedbackTopic = row.getString("feedback_topic");
        this.queryingTopic = row.getString("querying_topic");
        this.alexaSlotId = row.getString("alexa_slot_id");
        this.colorCode = row.getString("color_code");
        this.actions = new ArrayList<>();
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

    public String getFeedbackTopic() {
        return feedbackTopic;
    }

    public String getQueryingTopic() {
        return queryingTopic;
    }

    public String getAlexaSlotId() {
        return alexaSlotId;
    }

    public List<Action> getActions() {
        return actions;
    }

    public String getColorCode() {
        return colorCode;
    }
}
