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
    private final String colorCode;
    private final List<Feature> features;

    public Device(UUID id,
                  UUID circuitId,
                  String name,
                  Boolean isOnline,
                  String feedbackTopic) {
        this.id = id;
        this.circuitId = circuitId;
        this.name = name;
        this.isOnline = isOnline;
        this.feedbackTopic = feedbackTopic;
        this.colorCode = null;
        this.features = new ArrayList<>();
    }

    public Device(Row row) {
        this.id = row.getUUID("id");
        this.circuitId = row.getUUID("circuit_id");
        this.name = row.getString("name");
        this.isOnline = row.getBoolean("is_online");
        this.feedbackTopic = row.getString("feedback_topic");
        this.colorCode = row.getString("color_code");
        this.features = new ArrayList<>();
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

    public List<Feature> getFeatures() {
        return features;
    }

    public String getColorCode() {
        return colorCode;
    }
}
