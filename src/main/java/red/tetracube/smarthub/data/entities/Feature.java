package red.tetracube.smarthub.data.entities;

import red.tetracube.smarthub.enumerations.FeatureType;
import red.tetracube.smarthub.enumerations.RequestSourceType;
import java.util.*;

public class Feature {

    private UUID id;
    private String name;
    private FeatureType featureType;
    private boolean isRunning;
    private RequestSourceType sourceType;
    private String runningReferenceId;
    private UUID deviceId;

    private Device device;
    private List<Action> actions;
    private List<Telemetry> telemetryData;

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
}