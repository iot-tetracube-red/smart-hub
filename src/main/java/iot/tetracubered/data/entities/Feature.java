package iot.tetracubered.data.entities;

import io.vertx.mutiny.sqlclient.Row;
import iot.tetracubered.enumerations.FeatureType;
import iot.tetracubered.enumerations.RequestSourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Feature implements BaseEntity {

    private UUID id;
    private UUID featureId;
    private String name;
    private FeatureType featureType;
    private Float currentValue;
    private Boolean isRunning;
    private RequestSourceType sourceType;
    private String runningReferenceId;

    // External references
    private Device device;
    private List<Action> actions;

    public static Feature generateNewDevice(UUID featureId,
                                            String name,
                                            FeatureType featureType,
                                            Float currentValue,
                                            Device device) {
        var feature = new Feature();
        feature.id = UUID.randomUUID();
        feature.featureId = featureId;
        feature.name = name;
        feature.featureType = featureType;
        feature.currentValue = currentValue;
        feature.device = device;
        feature.isRunning = false;
        feature.sourceType = null;
        feature.runningReferenceId = null;
        feature.actions = new ArrayList<>();
        return feature;
    }

    @Override
    public void populateFromRow(Row row) {
        this.id = row.getUUID("id");
        this.featureId = row.getUUID("feature_id");
        this.name = row.getString("name");
        this.featureType = FeatureType.valueOf(row.getString("feature_type"));
        this.currentValue = row.getFloat("current_value");
        this.isRunning = row.getBoolean("is_running");
        this.runningReferenceId = row.getString("running_reference_id");
        this.sourceType = row.getString("source_type") == null
                ? null
                : RequestSourceType.valueOf(row.getString("source_type"));
    }

    public List<Action> getActions() {
        return actions;
    }

    public Float getCurrentValue() {
        return currentValue;
    }

    public Device getDevice() {
        return device;
    }

    public UUID getFeatureId() {
        return featureId;
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public UUID getId() {
        return id;
    }

    public Boolean getIsRunning() {
        return isRunning;
    }

    public String getName() {
        return name;
    }

    public String getRunningReferenceId() {
        return runningReferenceId;
    }

    public RequestSourceType getSourceType() {
        return sourceType;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void setCurrentValue(Float currentValue) {
        this.currentValue = currentValue;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public void setFeatureId(UUID featureId) {
        this.featureId = featureId;
    }

    public void setFeatureType(FeatureType featureType) {
        this.featureType = featureType;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setIsRunning(Boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRunningReferenceId(String runningReferenceId) {
        this.runningReferenceId = runningReferenceId;
    }

    public void setSourceType(RequestSourceType sourceType) {
        this.sourceType = sourceType;
    }
}
