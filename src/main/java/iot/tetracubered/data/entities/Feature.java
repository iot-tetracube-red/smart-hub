package iot.tetracubered.data.entities;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import iot.tetracubered.enumerations.FeatureType;
import iot.tetracubered.enumerations.RequestSourceType;

@Entity
public class Feature {

    @Id
    private UUID id;

    @Column(name = "feature_id", nullable = false, unique = true)
    private UUID featureId;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "feature_type", nullable = false)
    private FeatureType featureType;

    @Column(name = "current_value", nullable = false)
    private Float currentValue;

    @JoinColumn(name = "device_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Device.class)
    private Device device;

    @Column(name = "is_running")
    private Boolean isRunning;

    @Column(name = "source_type")
    private RequestSourceType sourceType;

    @Column(name = "running_reference_id")
    private String runningReferenceId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "feature", targetEntity = Action.class)
    private List<Action> actions;

    public Feature() {

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
