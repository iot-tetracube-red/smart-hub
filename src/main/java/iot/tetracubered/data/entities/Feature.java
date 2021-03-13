package iot.tetracubered.data.entities;

import iot.tetracubered.enumerations.FeatureType;
import iot.tetracubered.enumerations.RequestSourceType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "features")
public class Feature {

    @Id
    private UUID id;

    @Column(name = "feature_id", nullable = false)
    private UUID featureId;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "feature_type", nullable = false)
    private FeatureType featureType;

    @Column(name = "current_value", nullable = false)
    private Float currentValue;

    @Column(name = "is_running", nullable = false)
    private Boolean isRunning;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "source_type")
    private RequestSourceType sourceType;

    @Column(name = "source_reference_id")
    private String sourceReferenceId;

    @JoinColumn(name = "device_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Device.class)
    private Device device;

    @OneToMany(mappedBy = "feature", fetch = FetchType.LAZY, targetEntity = Action.class)
    private List<Action> actions = new ArrayList<>();

    public Feature() {
    }

    public Feature(UUID featureId,
                   String name,
                   FeatureType featureType,
                   Float currentValue,
                   Device device) {
        this.id = UUID.randomUUID();
        this.featureId = featureId;
        this.name = name;
        this.featureType = featureType;
        this.currentValue = currentValue;
        this.device = device;
        this.isRunning = false;
        this.sourceType = null;
        this.sourceReferenceId = null;
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

    public String getSourceReferenceId() {
        return sourceReferenceId;
    }

    public void setSourceReferenceId(String sourceReferenceId) {
        this.sourceReferenceId = sourceReferenceId;
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
