package iot.tetracubered.iotMessaging.deviceProvisioning.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import iot.tetracubered.enumerations.FeatureType;

import java.util.List;
import java.util.UUID;

public class DeviceFeatureProvisioningPayload {

    private final UUID featureId;
    private final String name;
    private final FeatureType featureType;
    private final Float value;
    private final List<DeviceFeatureActionProvisioningPayload> actions;

    @JsonCreator
    public DeviceFeatureProvisioningPayload(@JsonProperty("id") UUID featureId,
                                            @JsonProperty("name") String name,
                                            @JsonProperty("feature_type") FeatureType featureType,
                                            @JsonProperty("value") Float value,
                                            @JsonProperty("actions") List<DeviceFeatureActionProvisioningPayload> actions) {
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
