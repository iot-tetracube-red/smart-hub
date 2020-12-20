package iot.tetracube.data.entities;

import java.util.UUID;

public class DeviceAction {

    public UUID id;
    public UUID actionId;
    public String name;
    public boolean isDefault;
    public String publishTopic;
    public String alexaIntent;

    public UUID getId() {
        return id;
    }

    public UUID getActionId() {
        return actionId;
    }

    public String getName() {
        return name;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getPublishTopic() {
        return publishTopic;
    }

    public String getAlexaIntent() {
        return alexaIntent;
    }
}
