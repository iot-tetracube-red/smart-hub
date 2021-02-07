package iot.tetracubered.resources.bot.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.smallrye.common.constraint.NotNull;

import javax.validation.constraints.NotEmpty;

public class RunDeviceFeatureCommandRequest {

    @NotNull
    @NotEmpty
    private final String deviceName;

    @NotNull
    @NotEmpty
    private final String featureName;

    @NotNull
    @NotEmpty
    private final String commandName;

    @JsonCreator
    public RunDeviceFeatureCommandRequest(@JsonProperty("deviceName") String deviceName,
                                          @JsonProperty("featureName") String featureName,
                                          @JsonProperty("commandName") String commandName) {
        this.deviceName = deviceName;
        this.featureName = featureName;
        this.commandName = commandName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getFeatureName() {
        return featureName;
    }

    public String getCommandName() {
        return commandName;
    }
}
