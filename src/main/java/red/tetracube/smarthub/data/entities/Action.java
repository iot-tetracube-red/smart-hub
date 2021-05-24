package red.tetracube.smarthub.data.entities;

import java.util.*;

public class Action {

    private UUID id;
    private String name;
    private String triggerTopic;
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
}