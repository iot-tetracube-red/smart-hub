package iot.tetracubered.data.entities;

import io.vertx.mutiny.sqlclient.Row;
import iot.tetracubered.enumerations.FeatureType;
import iot.tetracubered.enumerations.RequestSourceType;

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
    private final Boolean isRunning;
    private final RequestSourceType sourceType;
    private final String runningReferenceId;
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
        this.isRunning = false;
        this.sourceType = null;
        this.runningReferenceId = null;
        this.actions = new ArrayList<>();
    }

    public Feature(Row row) {
        this.id = row.getUUID("id");
        this.featureId = row.getUUID("feature_id");
        this.deviceId = row.getUUID("device_id");
        this.name = row.getString("name");
        this.featureType = FeatureType.valueOf(row.getString("feature_type"));
        this.currentValue = row.getFloat("current_value");
        this.isRunning = row.getBoolean("is_running");
        this.sourceType = row.getString("running_source") == null
                ? null
                : RequestSourceType.valueOf(row.getString("running_source"));
        this.runningReferenceId = row.getString("running_reference_id");
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

    public Boolean getRunning() {
        return isRunning;
    }

    public RequestSourceType getSourceType() {
        return sourceType;
    }

    public String getRunningReferenceId() {
        return runningReferenceId;
    }
}
