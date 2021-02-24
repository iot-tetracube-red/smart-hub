package iot.tetracubered.data.entities;

import io.vertx.mutiny.sqlclient.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Device implements BaseEntity {

    private UUID id;
    private UUID circuitId;
    private String name;
    private Boolean isOnline;
    private String feedbackTopic;
    private String colorCode;

    // External references
    private List<Feature> features = new ArrayList<>();

    public static Device generateNewDevice(UUID circuitId,
                                           String name,
                                           String feedbackTopic) {
        var device = new Device();
        device.id = UUID.randomUUID();
        device.circuitId = circuitId;
        device.name = name;
        device.isOnline = true;
        device.feedbackTopic = feedbackTopic;
        device.features = new ArrayList<>();

        var obj = new Random();
        int rand_num = obj.nextInt(0xffffff + 1);
        device.colorCode = String.format("#%06x", rand_num);

        return device;
    }

    @Override
    public void populateFromRow(Row row) {
        this.id = row.getUUID("id");
        this.circuitId = row.getUUID("circuit_id");
        this.name = row.getString("name");
        this.isOnline = row.getBoolean("is_online");
        this.feedbackTopic = row.getString("feedback_topic");
        this.colorCode = row.getString("color_code");
    }

    public UUID getCircuitId() {
        return circuitId;
    }

    public String getColorCode() {
        return colorCode;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public String getFeedbackTopic() {
        return feedbackTopic;
    }

    public UUID getId() {
        return id;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public String getName() {
        return name;
    }

    public void setCircuitId(UUID circuitId) {
        this.circuitId = circuitId;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public void setFeedbackTopic(String feedbackTopic) {
        this.feedbackTopic = feedbackTopic;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public void setName(String name) {
        this.name = name;
    }
}
