package iot.tetracubered.data.entities;

import io.smallrye.common.constraint.NotNull;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Entity(name = "actions")
public class Action {

    @Id
    private UUID id;

    @NotNull
    private UUID actionId;

    @NotNull
    @NotEmpty
    private String triggerTopic;

    @NotNull
    @NotEmpty
    private String name;

    @ManyToOne(targetEntity = Feature.class, fetch = FetchType.LAZY)
    private Feature feature;

    public Action() {
    }

    public Action(UUID actionId,
                  String triggerTopic,
                  String name,
                  UUID featureId) {
        this.id = UUID.randomUUID();
        this.actionId = actionId;
        this.triggerTopic = triggerTopic;
        this.name = name;
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
}
