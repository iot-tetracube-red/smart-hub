package iot.tetracube.services;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import iot.tetracube.data.entities.Action;
import iot.tetracube.data.entities.Device;
import iot.tetracube.data.repositories.ActionRepository;
import iot.tetracube.models.messages.DeviceActionProvisioningMessage;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class ActionServices {

    private final static Logger LOGGER = LoggerFactory.getLogger(ActionServices.class);

    private final ActionRepository actionRepository;

    public ActionServices(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public Uni<Boolean> storeDeviceAction(Device parentDevice, List<DeviceActionProvisioningMessage> actionsToStore) {
        LOGGER.info("Processing " + actionsToStore.size() + " actions of device id " + parentDevice.getId());
        return Multi.createFrom().items(actionsToStore.stream())
                .onItem()
                .transformToMulti(actionToStore ->
                        this.manageActionProvisioningMessage(parentDevice.getId(), actionToStore).toMulti()
                )
                .concatenate().collectItems().asList()
                .map(responses -> responses.stream().allMatch(response -> response));
    }

    private Uni<Boolean> manageActionProvisioningMessage(UUID parentDeviceId, DeviceActionProvisioningMessage actionToStore) {
        var actionExistsUni = this.actionRepository.existsActionByHardwareId(actionToStore.getId());
        return actionExistsUni.flatMap(actionExists -> {
            if (actionExists == null) {
                return Uni.createFrom().item(false);
            } else if (actionExists) {
                LOGGER.info("Ignoring action: already exists");
                return Uni.createFrom().item(true);
            } else {
                LOGGER.info("Adding action");
                var action = new Action(
                        UUID.randomUUID(),
                        parentDeviceId,
                        actionToStore.getName(),
                        actionToStore.getId()
                );
                return this.actionRepository.saveAction(action)
                        .map(createdAction -> createdAction!= null);
            }
        });
    }

}
