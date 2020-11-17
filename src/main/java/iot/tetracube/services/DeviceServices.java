package iot.tetracube.services;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import iot.tetracube.broker.QueuesProducers;
import iot.tetracube.data.entities.Device;
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
    private final QueuesProducers queuesProducers;

    public DeviceServices(Jsonb jsonb,
                          DeviceRepository deviceRepository,
                          ActionServices actionServices,
                          QueuesProducers queuesProducers) {
        this.jsonb = jsonb;
        this.deviceRepository = deviceRepository;
        this.actionServices = actionServices;
        this.queuesProducers = queuesProducers;
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
                this.queuesProducers.sendDeviceFeedback(deviceProvisioningMessage.getCircuitId(), true);
                return this.deviceRepository.getDeviceByCircuitId(deviceProvisioningMessage.getCircuitId());
            } else {
                LOGGER.info("Storing device data in database");
                var deviceEntity = new Device(
                        UUID.randomUUID(),
                        deviceProvisioningMessage.getCircuitId(),
                        deviceProvisioningMessage.getDefaultName(),
                        false
                );
                var savedDevice =  this.deviceRepository.saveDevice(deviceEntity);
                this.queuesProducers.sendDeviceFeedback(deviceProvisioningMessage.getCircuitId(), true);
                return savedDevice;
            }
        });
        var actionSavedResultUni = deviceUni.flatMap(device -> {
            if (device == null) {
                return Uni.createFrom().item(false);
            }
            LOGGER.info("Device stored, now processing actions");
            var storeActionsResultUni =  this.actionServices.storeDeviceAction(device, deviceProvisioningMessage.getDeviceActionProvisioningMessages())
                    .invoke((storeActionsResult) -> {
                        this.queuesProducers.sendDeviceFeedback(deviceProvisioningMessage.getCircuitId(), storeActionsResult);
                    });
            return storeActionsResultUni;
        });
        actionSavedResultUni.subscribe()
                .with(device -> {
                    LOGGER.info("Processing promises chain");
                });
    }

}
