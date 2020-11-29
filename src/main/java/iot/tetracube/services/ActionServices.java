package iot.tetracube.services;

import io.smallrye.mutiny.Multi;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import iot.tetracube.data.repositories.ActionRepository;
import iot.tetracube.models.payloads.DeviceResponseAction;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class ActionServices {

    private final static Logger LOGGER = LoggerFactory.getLogger(ActionServices.class);

    private final ActionRepository actionRepository;

    public ActionServices(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public Multi<DeviceResponseAction> getDeviceActions(UUID deviceId) {
        return this.actionRepository.getActionsByDevice(deviceId)
                .onItem()
                .transform(action -> new DeviceResponseAction(action.getId(), action.getName()));
    }

}
