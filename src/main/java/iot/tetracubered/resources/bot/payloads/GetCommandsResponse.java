package iot.tetracubered.resources.bot.payloads;

public class GetCommandsResponse {

    private String deviceName;
    private final String featureName;
    private final String commandName;

    public GetCommandsResponse(String featureName,
                               String commandName) {
        this.featureName = featureName;
        this.commandName = commandName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getFeatureName() {
        return featureName;
    }

    public String getCommandName() {
        return commandName;
    }
}
