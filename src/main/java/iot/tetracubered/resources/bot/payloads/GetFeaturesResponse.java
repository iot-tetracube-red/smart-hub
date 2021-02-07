package iot.tetracubered.resources.bot.payloads;

public class GetFeaturesResponse {

    private final String deviceName;
    private final String featureName;

    public GetFeaturesResponse(String deviceName, String featureName) {
        this.deviceName = deviceName;
        this.featureName = featureName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getFeatureName() {
        return featureName;
    }
}
