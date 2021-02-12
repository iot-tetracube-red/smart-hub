package iot.tetracubered.data.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "devices")
public class Device {

    @Id
    private UUID id;

    @NotNull
    @Column(name = "circuit_id", unique = true, nullable = false)
    private UUID circuitId;

    @NotNull
    @NotEmpty
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @NotNull
    @Column(name = "is_online", nullable = false)
    private Boolean isOnline;

    @NotNull
    @NotEmpty
    @Column(name = "feedback_topic", unique = true, nullable = false)
    private String feedbackTopic;

    @Column(name = "color_code")
    private String colorCode;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = Feature.class, mappedBy = "device")
    private List<Feature> features = new ArrayList<>();

    public Device() {
    }

    public Device(UUID circuitId,
                  String name,
                  Boolean isOnline,
                  String feedbackTopic) {
        this.id = UUID.randomUUID();
        this.circuitId = circuitId;
        this.name = name;
        this.isOnline = isOnline;
        this.feedbackTopic = feedbackTopic;
        this.colorCode = null;
        this.features = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(UUID circuitId) {
        this.circuitId = circuitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    public String getFeedbackTopic() {
        return feedbackTopic;
    }

    public void setFeedbackTopic(String feedbackTopic) {
        this.feedbackTopic = feedbackTopic;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
}
