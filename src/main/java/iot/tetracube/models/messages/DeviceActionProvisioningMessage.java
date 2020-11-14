package iot.tetracube.models.messages;

import iot.tetracube.enumerations.ActionType;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import java.util.UUID;

public class DeviceActionProvisioningMessage {

    private UUID id;
    private String name;
    private ActionType type;

    @JsonbCreator
    public DeviceActionProvisioningMessage(@JsonbProperty("id") UUID id,
                                           @JsonbProperty("name") String name,
                                           @JsonbProperty("type") ActionType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

}
