package iot.tetracube.data.entities;

import io.vertx.mutiny.sqlclient.Row;

import java.util.UUID;

public class Action {

    private UUID id;
    private UUID deviceId;
    private String translationKey;
    private UUID hardwareId;
    private String alexaIntent;

    public Action(UUID id, UUID deviceId, String translationKey, UUID hardwareId) {
        this.id = id;
        this.deviceId = deviceId;
        this.translationKey = translationKey;
        this.hardwareId = hardwareId;
    }

    public Action(Row row) {
        this.id = row.getUUID("id");
        this.deviceId = row.getUUID("device_id");
        this.translationKey = row.getString("translation_key");
        this.hardwareId = row.getUUID("hardware_id");
        this.alexaIntent = row.getString("alexa_intent");
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public void setTranslationKey(String translationKey) {
        this.translationKey = translationKey;
    }

    public UUID getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(UUID hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getAlexaIntent() {
        return alexaIntent;
    }

    public void setAlexaIntent(String alexaIntent) {
        this.alexaIntent = alexaIntent;
    }

}
