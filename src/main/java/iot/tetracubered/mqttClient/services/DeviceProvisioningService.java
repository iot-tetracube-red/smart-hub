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
import java.util.List;
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

        var deviceUni = this.deviceRepository.existsByCircuitId(deviceProvisioningPayload.getId())
                .flatMap(deviceExists -> {
                    var device = new Device(
                            UUID.randomUUID(),
                            deviceProvisioningPayload.getId(),
                            deviceProvisioningPayload.getName(),
                            true,
                            deviceProvisioningPayload.getFeedbackTopic()
                    );
                    return deviceExists
                            ? this.deviceRepository.getDeviceByCircuitId(deviceProvisioningPayload.getId())
                            : this.deviceRepository.saveDevice(device);
                });

        var deviceWithFeatures = deviceUni.flatMap(device -> this.storeFeature(device, deviceProvisioningPayload.getFeatures()))
                .flatMap(features -> deviceUni.map(device -> device.getFeatures().addAll(features)));

        deviceWithFeatures.subscribe()
                .with(d -> LOGGER.info("Subscribed to events chains"));
    }

    private Uni<List<Feature>> storeFeature(Device device, List<DeviceFeatureProvisioningPayload> features) {
        return Multi.createFrom().items(features.stream())
                .flatMap(feature -> {
                    var storedFeatureUni = this.featureRepository.existsByDeviceAndFeatureId(device.getCircuitId(), feature.getFeatureId())
                            .flatMap(featureExists -> {
                                var newFeature = new Feature(
                                        UUID.randomUUID(),
                                        feature.getFeatureId(),
                                        feature.getName(),
                                        feature.getFeatureType(),
                                        feature.getValue(),
                                        device.getId()
                                );
                                return featureExists
                                        ? this.featureRepository.getFeatureByDeviceAndFeatureId(device.getCircuitId(), feature.getFeatureId())
                                        : this.featureRepository.saveFeature(newFeature);
                            });
                    var featureWithActions = this.storeAction(storedFeatureUni, feature.getActions());
                    return featureWithActions.toMulti();
                })
                .collectItems()
                .asList();
    }

    private Uni<Feature> storeAction(Uni<Feature> featureUni, List<DeviceFeatureActionProvisioningPayload> actionsToStore) {
        return featureUni.flatMap(feature ->
                Multi.createFrom().items(actionsToStore.stream())
                        .flatMap(actionToStore ->
                                this.actionRepository.actionExistsByFeatureAndActionId(feature.getFeatureId(), actionToStore.getActionId())
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
                                        .toMulti()
                        )
                        .collectItems()
                        .asList()
                        .map(actions -> {
                            feature.getActions().addAll(actions);
                            return feature;
                        })
        );
    }
}
