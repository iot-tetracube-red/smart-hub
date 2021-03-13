package iot.tetracubered.data.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    private UUID id;

    @Column(name = "circuit_id", nullable = false, unique = true)
    private UUID circuitId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_online", nullable = false)
    private Boolean isOnline;

    @Column(name = "feedback_topic", nullable = false, unique = true)
    private String feedbackTopic;

    @Column(name = "color_code")
    private String colorCode;

    @OneToMany(targetEntity = Feature.class, fetch = FetchType.LAZY, mappedBy = "device")
    private List<Feature> features = new ArrayList<>();

    public Device() {
    }

    public Device(UUID circuitId,
                  String name,
                  String feedbackTopic) {
        this.id = UUID.randomUUID();
        this.circuitId = circuitId;
        this.name = name;
        this.isOnline = true;
        this.feedbackTopic = feedbackTopic;

        var obj = new Random();
        int rand_num = obj.nextInt(0xffffff + 1);
        this.colorCode = String.format("#%06x", rand_num);
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
