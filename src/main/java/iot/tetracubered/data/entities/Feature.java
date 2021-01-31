package iot.tetracubered.data.entities;

import io.vertx.mutiny.sqlclient.Row;
import iot.tetracubered.enumerations.FeatureType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Feature {

    private final UUID id;
    private final UUID featureId;
    private final String name;
    private final FeatureType featureType;
    private final Float currentValue;
    private final UUID deviceId;
    private final List<Action> actions;

    public Feature(UUID id,
                   UUID featureId,
                   String name,
                   FeatureType featureType,
                   Float currentValue,
                   UUID deviceId) {
        this.id = id;
        this.featureId = featureId;
        this.name = name;
        this.featureType = featureType;
        this.currentValue = currentValue;
        this.deviceId = deviceId;
        this.actions = new ArrayList<>();
    }

    public Feature(Row row) {
        this.id = row.getUUID("id");
        this.featureId = row.getUUID("feature_id");
        this.deviceId = row.getUUID("device_id");
        this.name = row.getString("name");
        this.featureType = FeatureType.valueOf(row.getString("feature_type"));
        this.currentValue = row.getFloat("current_value");
        this.actions = new ArrayList<>();
    }

    public UUID getId() {
        return id;
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

    public Float getCurrentValue() {
        return currentValue;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public List<Action> getActions() {
        return actions;
    }
}
