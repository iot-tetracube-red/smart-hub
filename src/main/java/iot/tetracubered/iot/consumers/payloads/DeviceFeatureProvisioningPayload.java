package iot.tetracubered.iot.consumers.payloads;

import iot.tetracubered.enumerations.FeatureType;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import java.util.List;
import java.util.UUID;

public class DeviceFeatureProvisioningPayload {

    private final UUID featureId;
    private final String name;
    private final FeatureType featureType;
    private final Float value;
    private final List<DeviceFeatureActionProvisioningPayload> actions;

    @JsonbCreator
    public DeviceFeatureProvisioningPayload(@JsonbProperty("id") UUID featureId,
                                            @JsonbProperty("name") String name,
                                            @JsonbProperty("feature_type") FeatureType featureType,
                                            @JsonbProperty("value") Float value,
                                            @JsonbProperty("actions") List<DeviceFeatureActionProvisioningPayload> actions) {
        this.featureId = featureId;
        this.name = name;
        this.featureType = featureType;
        this.value = value;
        this.actions = actions;
    }

    public UUID getFeatureId() {
        return featureId;
    }

    public String getName() {
        return name;
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public Float getValue() {
        return value;
    }

    public List<DeviceFeatureActionProvisioningPayload> getActions() {
        return actions;
    }
}
