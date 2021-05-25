package red.tetracube.smarthub.data.entities;

import java.time.LocalDateTime;
import java.util.UUID;

public class Telemetry {

    private UUID id;
    private Float value;
    private LocalDateTime storedAt;
    private LocalDateTime probedAt;
    private UUID featureId;

    private Feature feature;

    public Telemetry(UUID id,
                     Float value,
                     LocalDateTime storedAt,
                     LocalDateTime probedAt,
                     UUID featureId) {
        this.id = id;
        this.value = value;
        this.storedAt = storedAt;
        this.probedAt = probedAt;
        this.featureId = featureId;
    }

    public UUID getId() {
        return id;
    }

    public Float getValue() {
        return value;
    }

    public LocalDateTime getStoredAt() {
        return storedAt;
    }

    public LocalDateTime getProbedAt() {
        return probedAt;
    }

    public UUID getFeatureId() {
        return featureId;
    }

    public Feature getFeature() {
        return feature;
    }
}
