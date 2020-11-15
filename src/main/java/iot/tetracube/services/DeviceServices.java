package iot.tetracube.services;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import iot.tetracube.data.entities.Device;
import iot.tetracube.data.repositories.ActionRepository;
import iot.tetracube.data.repositories.DeviceRepository;
import iot.tetracube.models.messages.DeviceProvisioningMessage;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
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
    public void manageDeviceProvisioningMessage(String message) {
        DeviceProvisioningMessage deviceProvisioningMessage;
        try {
            deviceProvisioningMessage = this.jsonb.fromJson(message, DeviceProvisioningMessage.class);
        } catch (Exception e) {
            LOGGER.error("Cannot convert message, ignoring it");
            return;
        }
        LOGGER.info("Checking if device already exists");
        var deviceExistsUni = this.deviceRepository.deviceExistsByCircuitId(deviceProvisioningMessage.getCircuitId());
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
        var actionSavedResultUni = deviceUni.flatMap(device -> {
            if (device == null) {
                return Uni.createFrom().item(false);
            }
            LOGGER.info("Device stored, now processing actions");
            return this.actionServices.storeDeviceAction(device, deviceProvisioningMessage.getDeviceActionProvisioningMessages());
        });
        actionSavedResultUni.subscribe()
                .with(device -> {
                    LOGGER.info("Processing promises chain");
                });
    }

}
