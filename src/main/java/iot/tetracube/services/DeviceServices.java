package iot.tetracube.services;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import iot.tetracube.data.entities.Device;
import iot.tetracube.data.repositories.DeviceRepository;
import iot.tetracube.models.dto.ManageDeviceProvisioningResponse;
import iot.tetracube.models.messages.DeviceProvisioningMessage;
import iot.tetracube.models.payloads.DeviceResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import java.util.ArrayList;
import java.util.UUID;

@ApplicationScoped
public class DeviceServices {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceServices.class);

    private final Jsonb jsonb;
    private final DeviceRepository deviceRepository;
    private final ActionServices actionServices;

    public DeviceServices(Jsonb jsonb,
                          DeviceRepository deviceRepository,
                          ActionServices actionServices) {
        this.jsonb = jsonb;
        this.deviceRepository = deviceRepository;
        this.actionServices = actionServices;
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
        var deviceExistsUni = this.deviceRepository.deviceExistsByCircuitId(deviceProvisioningMessage.getCircuitId());

        LOGGER.info("Save or update existing device");
        var deviceUni = deviceExistsUni.flatMap(deviceExists -> {
            if (deviceExists == null) {
                LOGGER.error("There was some error during device verification, returning bad feedback to device");
                return Uni.createFrom().nullItem();
            } else if (deviceExists) {
                LOGGER.info("Device exists, updating only its actions if needed");
                return this.deviceRepository.getDeviceByCircuitId(deviceProvisioningMessage.getCircuitId());
            } else {
                LOGGER.info("Storing device data in database");
                var deviceEntity = new Device(
                        UUID.randomUUID(),
                        deviceProvisioningMessage.getCircuitId(),
                        deviceProvisioningMessage.getDefaultName(),
                        false
                );
                return this.deviceRepository.saveDevice(deviceEntity);
            }
        });

        LOGGER.info("Store device actions");
        var actionSavedResultUni = deviceUni.flatMap(device -> {
            if (device == null) {
                return Uni.createFrom().item(new ManageDeviceProvisioningResponse(deviceProvisioningMessage.getCircuitId(), false));
            }
            LOGGER.info("Device stored, now processing actions");
            return this.actionServices.storeDeviceAction(device, deviceProvisioningMessage.getDeviceActionProvisioningMessages())
                    .map(result -> new ManageDeviceProvisioningResponse(device.getCircuitId(), result));
        });

        LOGGER.info("Subscribing to events");
        actionSavedResultUni
                .invoke(device -> {
                    LOGGER.info("Processing promises chain");
                });

        return actionSavedResultUni;
    }

    public Multi<DeviceResponse> getDevices() {
        return this.deviceRepository.getDevices()
                .map(device ->
                        new DeviceResponse(
                                device.getId(),
                                device.getName(),
                                new ArrayList<>()
                        )
                )
                .onItem()
                .call(device ->
                        this.actionServices.getDeviceActions(device.getId())
                                .collectItems().asList()
                                .onItem()
                                .invoke(actions -> device.getActions().addAll(actions))
                );
    }

}
