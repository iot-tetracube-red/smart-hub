package red.tetracube.smarthub.data.entities;

import java.util.Calendar;
import java.util.UUID;

public class Telemetry {

    private UUID id;
    private Float value;
    private Calendar storedAt;
    private Calendar probedAt;
    private UUID featureId;

    private Feature feature;

    public Telemetry(UUID id,
                     Float value,
                     Calendar storedAt,
                     Calendar probedAt,
                     UUID featureId) {
        this.id = id;
        this.value = value;
        this.storedAt = storedAt;
        this.probedAt = probedAt;
        this.featureId = featureId;
    }
}
