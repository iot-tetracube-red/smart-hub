package iot.tetracube.deviceComunication.deviceProvisioning;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import iot.tetracube.data.entities.Action;
import iot.tetracube.data.entities.Device;
import iot.tetracube.data.repositories.ActionRepository;
import iot.tetracube.data.repositories.DeviceRepository;
import iot.tetracube.models.dto.ManageDeviceProvisioningResponse;
import iot.tetracube.deviceComunication.deviceProvisioning.payloads.DeviceActionProvisioningMessage;
import iot.tetracube.deviceComunication.deviceProvisioning.payloads.DeviceProvisioningMessage;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class DeviceProvisioningService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceProvisioningService.class);

    private final Jsonb jsonb;
    private final DeviceRepository deviceRepository;
    private final ActionRepository actionRepository;

    public DeviceProvisioningService(Jsonb jsonb,
                                     DeviceRepository deviceRepository,
                                     ActionRepository actionRepository) {
        this.jsonb = jsonb;
        this.deviceRepository = deviceRepository;
        this.actionRepository = actionRepository;
    }

    @ConsumeEvent("device-provisioning")
    public Uni<ManageDeviceProvisioningResponse> manageDeviceProvisioningMessage(String message) {
        LOGGER.info("Parsing device provisioning message");
        DeviceProvisioningMessage deviceProvisioningMessage;
        try {
            deviceProvisioningMessage = this.jsonb.fromJson(message, DeviceProvisioningMessage.class);
        } catch (Exception e) {
            LOGGER.error("Cannot convert message, ignoring it");
            return Uni.createFrom().nullItem();
        }

        LOGGER.info("Checking if device already exists");
        var deviceExistsUni = this.deviceRepository.deviceExistsByCircuitId(deviceProvisioningMessage.getId());

        LOGGER.info("Save or update existing device");
        var deviceUni = deviceExistsUni.flatMap(deviceExists -> {
            if (deviceExists == null) {
                LOGGER.error("There was some error during device verification, returning bad feedback to device");
                return Uni.createFrom().nullItem();
            } else if (deviceExists) {
                LOGGER.info("Device exists, updating only its actions if needed");
                return this.deviceRepository.getDeviceByCircuitId(deviceProvisioningMessage.getId());
            } else {
                LOGGER.info("Storing device data in database");
                var deviceEntity = new Device(
                        UUID.randomUUID(),
                        deviceProvisioningMessage.getId(),
                        deviceProvisioningMessage.getDefaultName(),
                        false,
                        deviceProvisioningMessage.getHostname()
                );
                return this.deviceRepository.saveDevice(deviceEntity);
            }
        });

        LOGGER.info("Store device actions");
        var actionSavedResultUni = deviceUni.flatMap(device -> {
            if (device == null) {
                return Uni.createFrom().item(new ManageDeviceProvisioningResponse(deviceProvisioningMessage.getId(), false));
            }
            LOGGER.info("Device stored, now processing actions");
            return this.storeDeviceAction(device, deviceProvisioningMessage.getDeviceActionProvisioningMessages())
                    .map(result -> new ManageDeviceProvisioningResponse(device.getCircuitId(), result));
        });

        LOGGER.info("Subscribing to events");
        actionSavedResultUni
                .invoke(device -> {
                    LOGGER.info("Processing promises chain");
                });

        return actionSavedResultUni;
    }

    private Uni<Boolean> storeDeviceAction(Device parentDevice, List<DeviceActionProvisioningMessage> actionsToStore) {
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
                        actionToStore.getTopic(),
                        actionToStore.getDefaultName(),
                        actionToStore.getId()
                );
                return this.actionRepository.saveAction(action)
                        .map(Objects::nonNull);
            }
        });
    }

}
