package iot.tetracubered.data.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "actions")
public class Action {

    @Id
    private UUID id;

    @Column(name = "action_id", nullable = false, unique = true)
    private UUID actionId;

    @Column(name = "trigger_topic", nullable = false, unique = true)
    private String triggerTopic;

    @Column(name = "name", nullable = false)
    private String name;

    @JoinColumn(name = "feature_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Feature.class)
    private Feature feature;

    public Action() {

    }

    public Action(UUID id, UUID actionId, String triggerTopic, String name, Feature feature) {
        this.id = id;
        this.actionId = actionId;
        this.triggerTopic = triggerTopic;
        this.name = name;
        this.feature = feature;
    }

    public UUID getActionId() {
        return actionId;
    }

    public Feature getFeature() {
        return feature;
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

    public void setActionId(UUID actionId) {
        this.actionId = actionId;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTriggerTopic(String triggerTopic) {
        this.triggerTopic = triggerTopic;
    }
}
