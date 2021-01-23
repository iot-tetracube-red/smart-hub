package iot.tetracubered.resources.actions;

import io.smallrye.mutiny.Multi;
import iot.tetracubered.data.repositories.ActionRepository;
import iot.tetracubered.resources.actions.payloads.DeviceActionResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class ActionBusinessServices {

    @Inject
    ActionRepository actionRepository;

    public Multi<DeviceActionResponse> getDeviceActions(UUID deviceId) {
        return this.actionRepository.getDeviceActions(deviceId)
                .map(action -> new DeviceActionResponse(action.getId(), action.getName(), action.getValue()));
    }
}
