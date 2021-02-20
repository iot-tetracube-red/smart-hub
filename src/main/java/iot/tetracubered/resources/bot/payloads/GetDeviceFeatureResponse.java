package iot.tetracubered.resources.bot.payloads;

import javax.json.bind.annotation.JsonbProperty;

public class GetDeviceFeatureResponse {

    @JsonbProperty("deviceName")
    private String deviceName;

    @JsonbProperty("featureName")
    private String featureName;

    public GetDeviceFeatureResponse(String deviceName,
                                    String featureName) {
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
