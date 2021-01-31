package iot.tetracubered.mqttClient.services;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import iot.tetracubered.data.entities.Action;
import iot.tetracubered.data.entities.Device;
import iot.tetracubered.data.entities.Feature;
import iot.tetracubered.data.repositories.ActionRepository;
import iot.tetracubered.data.repositories.DeviceRepository;
import iot.tetracubered.data.repositories.FeatureRepository;
import iot.tetracubered.mqttClient.payloads.DeviceFeatureActionProvisioningPayload;
import iot.tetracubered.mqttClient.payloads.DeviceFeatureProvisioningPayload;
import iot.tetracubered.mqttClient.payloads.DeviceProvisioningPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class DeviceProvisioningService {

    @Inject
    DeviceRepository deviceRepository;

    @Inject
    FeatureRepository featureRepository;

    @Inject
    ActionRepository actionRepository;

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceProvisioningService.class);

    @ConsumeEvent("device-provisioning")
    public void deviceProvisioning(DeviceProvisioningPayload deviceProvisioningPayload) {
        LOGGER.info("Trying to store device with circuit id: " + deviceProvisioningPayload.getId());
        var device = new Device(
                UUID.randomUUID(),
                deviceProvisioningPayload.getId(),
                deviceProvisioningPayload.getName(),
                true,
                deviceProvisioningPayload.getFeedbackTopic()
        );

        var deviceUni = this.deviceRepository.existsByCircuitId(deviceProvisioningPayload.getId())
                .flatMap(deviceExists -> {
                    if (deviceExists) {
                        LOGGER.info("Device already exists no need to store again");
                        return this.deviceRepository.getDeviceByCircuitId(deviceProvisioningPayload.getId());
                    }
                    LOGGER.info("Storing a new device");
                    return this.deviceRepository.saveDevice(device);
                })
                .flatMap(storedDevices ->
                        Multi.createFrom().items(deviceProvisioningPayload.getFeatures().stream())
                                .flatMap(feature -> this.storeFeature(storedDevices, feature))
                                .map(storedFeature -> {
                                    storedDevices.getFeatures().add(storedFeature);
                                    return storedDevices;
                                })
                                .toUni()
                );

        deviceUni.subscribe()
                .with(d -> LOGGER.info("Device stored successfully with id: " + d.getId()));
    }

    private Multi<Feature> storeFeature(Device device, DeviceFeatureProvisioningPayload featureToStore) {
        var featureUni = this.featureRepository.existsByDeviceAndFeatureId(device.getCircuitId(), featureToStore.getFeatureId())
                .flatMap(exists -> {
                    if (exists) {
                        LOGGER.info("Feature already exists no need to store again");
                        return this.featureRepository.getFeatureByDeviceAndFeatureId(device.getCircuitId(), featureToStore.getFeatureId());
                    }
                    LOGGER.info("Storing the new feature");
                    var feature = new Feature(
                            UUID.randomUUID(),
                            featureToStore.getFeatureId(),
                            featureToStore.getName(),
                            featureToStore.getFeatureType(),
                            featureToStore.getValue(),
                            device.getId()
                    );
                    return this.featureRepository.saveFeature(feature);
                })
                .flatMap(storedFeature ->
                        Multi.createFrom().items(featureToStore.getActions().stream())
                                .flatMap(action -> this.storeAction(storedFeature, action))
                                .map(storedActions -> {
                                    storedFeature.getActions().add(storedActions);
                                    return storedFeature;
                                })
                                .toUni()
                );

        return featureUni.toMulti();
    }

    private Multi<Action> storeAction(Feature feature, DeviceFeatureActionProvisioningPayload actionToStore) {
        return this.actionRepository.actionExistsByFeatureAndActionId(feature.getFeatureId(), actionToStore.getActionId())
                .flatMap(actionExists -> {
                    if (actionExists) {
                        LOGGER.info("Action already exists no need to store again");
                        return this.actionRepository.getActionByFeatureAndActionId(feature.getFeatureId(), actionToStore.getActionId());
                    }

                    LOGGER.info("Storing the new action");
                    var action = new Action(
                            UUID.randomUUID(),
                            actionToStore.getActionId(),
                            actionToStore.getTriggerTopic(),
                            actionToStore.getName(),
                            feature.getId()
                    );
                    return this.actionRepository.saveAction(action);
                })
                .toMulti();
    }
}
