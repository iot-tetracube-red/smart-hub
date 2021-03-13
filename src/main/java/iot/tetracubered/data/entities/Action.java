package iot.tetracubered.data.entities;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "actions")
public class Action {

    @Id
    private UUID id;

    @Column(name = "action_id", nullable = false, unique = true)
    private UUID actionId;

    @Column(name = "trigger_topic", nullable = false, unique = true)
    private String triggerTopic;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @JoinColumn(name = "feature_id", nullable = false)
    @ManyToOne(targetEntity = Feature.class, fetch = FetchType.LAZY)
    private Feature feature;

    public Action() {
    }

    public Action(UUID actionId,
                  String triggerTopic,
                  String name,
                  Feature feature) {
        this.id = UUID.randomUUID();
        this.actionId = actionId;
        this.triggerTopic = triggerTopic;
        this.name = name;
        this.feature = feature;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getActionId() {
        return actionId;
    }

    public void setActionId(UUID actionId) {
        this.actionId = actionId;
    }

    public String getTriggerTopic() {
        return triggerTopic;
    }

    public void setTriggerTopic(String triggerTopic) {
        this.triggerTopic = triggerTopic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }
}
