package red.tetracube.smarthub.data.entities;

import red.tetracube.smarthub.annotations.Column;
import red.tetracube.smarthub.annotations.Entity;

import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Device {

    @Column()
    private UUID id;

    @Column()
    private String name;

    @Column(name = "is_online")
    private Boolean isOnline;

    @Column(name = "feedback_topic")
    private String feedbackTopic;

    @Column(name = "color_code")
    private String colorCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private List<Feature> features;

    public Device() {
    }

    public Device(UUID id,
                  String name,
                  String feedbackTopic,
                  LocalDateTime createdAt,
                  LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.isOnline = true;
        this.feedbackTopic = feedbackTopic;
        this.colorCode = String.format("#%06x",new Random().nextInt(0xffffff + 1));
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public String getFeedbackTopic() {
        return feedbackTopic;
    }

    public String getColorCode() {
        return colorCode;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}