package iot.tetracubered.resources.bot.payloads;

import java.util.ArrayList;
import java.util.List;

public class GetCommandsResponse {

    private final String deviceName;
    private final String featureName;
    private final List<String> commands = new ArrayList<>();

    public GetCommandsResponse(String deviceName,
                               String featureName) {
        this.featureName = featureName;
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getFeatureName() {
        return featureName;
    }

    public List<String> getCommands() {
        return commands;
    }
}
