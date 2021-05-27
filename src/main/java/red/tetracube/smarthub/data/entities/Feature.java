package red.tetracube.smarthub.data.entities;

import red.tetracube.smarthub.annotations.Column;
import red.tetracube.smarthub.annotations.Entity;
import red.tetracube.smarthub.enumerations.FeatureType;
import red.tetracube.smarthub.enumerations.RequestSourceType;
import java.util.*;

@Entity
public class Feature {

    @Column
    private UUID id;

    @Column
    private String name;

    @Column(name = "feature_type")
    private FeatureType featureType;

    @Column(name = "is_running")
    private Boolean isRunning;

    @Column(name = "source_type")
    private RequestSourceType sourceType;

    @Column(name = "running_reference_id")
    private String runningReferenceId;

    @Column(name = "device_id")
    private UUID deviceId;

    private Device device;
    private List<Action> actions;
    private List<Telemetry> telemetryData;

    public Feature() {
    }

    public Feature(UUID id,
                   String name,
                   FeatureType featureType,
                   boolean isRunning,
                   RequestSourceType sourceType,
                   String runningReferenceId,
                   UUID deviceId) {
        this.id = id;
        this.name = name;
        this.featureType = featureType;
        this.isRunning = isRunning;
        this.sourceType = sourceType;
        this.runningReferenceId = runningReferenceId;
        this.deviceId = deviceId;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public RequestSourceType getSourceType() {
        return sourceType;
    }

    public String getRunningReferenceId() {
        return runningReferenceId;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public Device getDevice() {
        return device;
    }

    public List<Action> getActions() {
        return actions;
    }

    public List<Telemetry> getTelemetryData() {
        return telemetryData;
    }

    public void setFeatureType(FeatureType featureType) {
        this.featureType = featureType;
    }

    public void setRunning(Boolean running) {
        isRunning = running;
    }

    public void setSourceType(RequestSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public void setRunningReferenceId(String runningReferenceId) {
        this.runningReferenceId = runningReferenceId;
    }
}