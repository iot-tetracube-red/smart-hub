package red.tetracube.smarthub.iot.dto;

import red.tetracube.smarthub.enumerations.RequestSourceType;

import java.util.UUID;

public record ActionTriggerMessage(
        UUID actionId,
        String actionName,
        String topic,
        RequestSourceType sourceType,
        String sourceId,
        UUID featureId
) {
}
