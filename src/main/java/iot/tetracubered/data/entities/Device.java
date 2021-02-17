package iot.tetracubered.data.entities;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Device {

    @Id
    private UUID id;

    @Column(name = "circuit_id", nullable = false, unique = true)
    private UUID circuitId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "is_online", nullable = false)
    private Boolean isOnline;

    @Column(name = "feedback_topic", nullable = false)
    private String feedbackTopic;

    @Column(name = "color_code")
    private String colorCode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device", targetEntity = Feature.class)
    private List<Feature> features;

    public Device() {

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
