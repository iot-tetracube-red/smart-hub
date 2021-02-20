package iot.tetracubered.resources.bot.payloads;

import javax.json.bind.annotation.JsonbProperty;
import java.util.ArrayList;
import java.util.List;

public class GetCommandsResponse {

    @JsonbProperty("deviceName")
    private String deviceName;

    @JsonbProperty("featureName")
    private String featureName;

    @JsonbProperty("commands")
    private final List<String> commands = new ArrayList<>();

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public List<String> getCommands() {
        return commands;
    }
}
