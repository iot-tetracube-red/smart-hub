package iot.tetracubered.data.entities;

import io.vertx.mutiny.sqlclient.Row;
import iot.tetracubered.enumerations.FeatureType;

import java.util.UUID;

public class Feature {

    private final UUID id;
    private final UUID featureId;
    private final String commandTopic;
    private final String queryTopic;
    private final String name;
    private final FeatureType featureType;
    private final Float currentValue;
    private final UUID circuitId;

    public Feature(Row row) {
        this.id = row.getUUID("id");
        this.featureId = row.getUUID("feature_id");
        this.circuitId = row.getUUID("circuit_id");
        this.commandTopic = row.getString("command_topic");
        this.queryTopic = row.getString("query_topic");
        this.name = row.getString("name");
        this.featureType = FeatureType.valueOf(row.getString("feature_type"));
        this.currentValue = row.getFloat("current_value");
    }

    public UUID getId() {
        return id;
    }

    public UUID getFeatureId() {
        return featureId;
    }

    public String getCommandTopic() {
        return commandTopic;
    }

    public String getQueryTopic() {
        return queryTopic;
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

    public UUID getCircuitId() {
        return circuitId;
    }
}
