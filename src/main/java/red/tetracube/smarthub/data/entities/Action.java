package red.tetracube.smarthub.data.entities;

import red.tetracube.smarthub.annotations.Column;
import red.tetracube.smarthub.annotations.Entity;

import java.util.*;

@Entity
public class Action {

    @Column
    private UUID id;

    @Column
    private String name;

    @Column(name = "trigger_topic")
    private String triggerTopic;

    @Column(name = "feature_id")
    private UUID featureId;

    private Feature feature;

    public Action() {
    }

    public Action(UUID id, String name, String triggerTopic, UUID featureId) {
        this.id = id;
        this.name = name;
        this.triggerTopic = triggerTopic;
        this.featureId = featureId;
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

    public UUID getFeatureId() {
        return featureId;
    }

    public Feature getFeature() {
        return feature;
    }
}