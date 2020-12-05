package iot.tetracube.messagingInterface.deviceProvisioning;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import iot.tetracube.data.entities.Device;
import iot.tetracube.data.repositories.DeviceRepository;
import iot.tetracube.messagingInterface.deviceProvisioning.dto.ManageDeviceProvisioningResponse;
import iot.tetracube.messagingInterface.deviceProvisioning.payloads.DeviceProvisioningMessage;

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
    // private final ActionRepository actionRepository;

    public DeviceProvisioningService(Jsonb jsonb,
                                     DeviceRepository deviceRepository/*,
                                     ActionRepository actionRepository*/) {
        this.jsonb = jsonb;
        this.deviceRepository = deviceRepository;
        // this.actionRepository = actionRepository;
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
                return this.deviceRepository.createDevice(deviceEntity);
            }
        });

        return deviceUni.map( c-> {
            var tmp = c;
            return null;
        });
    }
}
