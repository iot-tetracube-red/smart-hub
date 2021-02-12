package iot.tetracubered.data.entities;

import io.smallrye.common.constraint.NotNull;
import iot.tetracubered.enumerations.FeatureType;
import iot.tetracubered.enumerations.RequestSourceType;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "features")
public class Feature {

    @Id
    private UUID id;

    @NotNull
    @Column(name = "feature_id", nullable = false, unique = true)
    private UUID featureId;

    @NotNull
    @NotEmpty
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "feature_type", nullable = false)
    private FeatureType featureType;

    @NotNull
    @Column(name = "current_value", nullable = false)
    private Float currentValue;

    @NotNull
    @Column(name = "is_running")
    private Boolean isRunning;

    @Enumerated(EnumType.STRING)
    @Column(name = "running_source")
    private RequestSourceType sourceType;

    @Column(name = "running_reference_id")
    private String runningReferenceId;

    @JoinColumn(name = "device_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Device.class)
    private Device device;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = Action.class, mappedBy = "feature")
    private List<Action> actions = new ArrayList<>();

    public Feature() {
    }

    public Feature(UUID featureId,
                   String name,
                   FeatureType featureType,
                   Float currentValue,
                   UUID deviceId) {
        this.id = UUID.randomUUID();
        this.featureId = featureId;
        this.name = name;
        this.featureType = featureType;
        this.currentValue = currentValue;
        this.isRunning = false;
        this.sourceType = null;
        this.runningReferenceId = null;
        this.actions = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFeatureId() {
        return featureId;
    }

    public void setFeatureId(UUID featureId) {
        this.featureId = featureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public void setFeatureType(FeatureType featureType) {
        this.featureType = featureType;
    }

    public Float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Float currentValue) {
        this.currentValue = currentValue;
    }

    public Boolean getRunning() {
        return isRunning;
    }

    public void setRunning(Boolean running) {
        isRunning = running;
    }

    public RequestSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(RequestSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getRunningReferenceId() {
        return runningReferenceId;
    }

    public void setRunningReferenceId(String runningReferenceId) {
        this.runningReferenceId = runningReferenceId;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}
