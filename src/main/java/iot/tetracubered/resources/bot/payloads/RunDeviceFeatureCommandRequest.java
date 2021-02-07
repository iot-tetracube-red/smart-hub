package iot.tetracubered.resources.bot.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.smallrye.common.constraint.NotNull;
import iot.tetracubered.enumerations.RequestSourceType;

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

    @NotNull
    @NotEmpty
    private final String referenceId;

    @NotNull
    @NotEmpty
    private final RequestSourceType source;

    @JsonCreator
    public RunDeviceFeatureCommandRequest(@JsonProperty("deviceName") String deviceName,
                                          @JsonProperty("featureName") String featureName,
                                          @JsonProperty("commandName") String commandName,
                                          @JsonProperty("referenceId") String referenceId,
                                          @JsonProperty("source") RequestSourceType source) {
        this.deviceName = deviceName;
        this.featureName = featureName;
        this.commandName = commandName;
        this.referenceId = referenceId;
        this.source = source;
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

    public String getReferenceId() {
        return referenceId;
    }

    public RequestSourceType getSource() {
        return source;
    }
}
