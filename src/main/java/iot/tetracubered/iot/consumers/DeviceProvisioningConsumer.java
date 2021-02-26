package iot.tetracubered.iot.consumers;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import iot.tetracubered.data.entities.Action;
import iot.tetracubered.data.entities.Device;
import iot.tetracubered.data.entities.Feature;
import iot.tetracubered.data.repositories.ActionRepository;
import iot.tetracubered.data.repositories.DeviceRepository;
import iot.tetracubered.data.repositories.FeatureRepository;
import iot.tetracubered.iot.consumers.payloads.DeviceFeatureActionProvisioningPayload;
import iot.tetracubered.iot.consumers.payloads.DeviceFeatureProvisioningPayload;
import iot.tetracubered.iot.consumers.payloads.DeviceProvisioningPayload;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import java.util.List;

@ApplicationScoped
public class DeviceProvisioningConsumer {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceProvisioningConsumer.class);

    @Inject
    Jsonb jsonb;

    @Inject
    EventBus eventBus;

    @Inject
    DeviceRepository deviceRepository;

    @Inject
    FeatureRepository featureRepository;

    @Inject
    ActionRepository actionRepository;

    @Incoming("devices-provisioning")
    public void deviceProvisioning(byte[] payload) {
        LOGGER.info("Arrived message in device-provisioning topic: " + new String(payload));

        DeviceProvisioningPayload deviceProvisioningPayload;
        try {
            deviceProvisioningPayload = this.jsonb.fromJson(new String(payload), DeviceProvisioningPayload.class);
        } catch (Exception ex) {
            LOGGER.info("Cannot read payload content, ignoring it");
            return;
        }

        LOGGER.info("Trying to store device with circuit id: " + deviceProvisioningPayload.getId());
        this.storeDevice(deviceProvisioningPayload)
                .subscribe()
                .with(storedDevice -> {
                    LOGGER.info(
                            "Device stored with id {} and {} features",
                            storedDevice.getId(),
                            storedDevice.getFeatures().size()
                    );
                });
    }

    private Uni<Device> storeDevice(DeviceProvisioningPayload deviceProvisioningPayload) {
        LOGGER.info("Check if device exists giving CircuitId");
        return this.deviceRepository.existsByCircuitId(deviceProvisioningPayload.getId())
                .flatMap(deviceExists -> {
                    var dbDeviceToStore = Device.generateNewDevice(
                            deviceProvisioningPayload.getId(),
                            deviceProvisioningPayload.getName(),
                            deviceProvisioningPayload.getFeedbackTopic()
                    );
                    return !deviceExists
                            ? this.deviceRepository.storeDevice(dbDeviceToStore)
                            : this.deviceRepository.updateDevice(deviceProvisioningPayload.getId(), dbDeviceToStore);
                })
                .flatMap(device ->
                        this.storeFeatures(device, deviceProvisioningPayload.getFeatures())
                );
    }

    private Uni<Device> storeFeatures(Device device, List<DeviceFeatureProvisioningPayload> featuresPayload) {
        return Multi.createFrom().items(featuresPayload.parallelStream())
                .call(feature -> {
                    return this.featureRepository.existsByFeatureId(feature.getFeatureId());
                })
                .flatMap(feature -> {
                    var newFeature =  Feature.generateNewDevice(
                            feature.getFeatureId(),
                            feature.getName(),
                            feature.getFeatureType(),
                            feature.getValue(),
                            device
                    );

                    return this.featureRepository.storeFeature(newFeature)
                            .flatMap(dbFeature -> this.storeAction(dbFeature, feature.getActions()))
                            .toMulti();
                })
                .collect().asList()
                .map(features -> {
                    device.setFeatures(features);
                    return device;
                });
    }

    private Uni<Feature> storeAction(Feature feature, List<DeviceFeatureActionProvisioningPayload> actionsToStore) {
        return Multi.createFrom().items(actionsToStore.parallelStream())
                .flatMap(actionToStore -> {
                    var action = Action.generateNewAction(
                            actionToStore.getActionId(),
                            actionToStore.getTriggerTopic(),
                            actionToStore.getName(),
                            feature
                    );
                    return this.actionRepository.storeAction(action)
                            .toMulti();
                })
                .collect().asList()
                .map(actions -> {
                    feature.setActions(actions);
                    return feature;
                });
    }

}
