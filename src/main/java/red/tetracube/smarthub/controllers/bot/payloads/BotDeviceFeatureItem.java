package red.tetracube.smarthub.controllers.bot.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BotDeviceFeatureItem(
        @JsonProperty("deviceName") String deviceName,
        @JsonProperty("featureName") String featureName,
        @JsonProperty("color") String color
) {

}
