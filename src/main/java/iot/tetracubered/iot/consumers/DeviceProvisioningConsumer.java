package iot.tetracubered.iot.consumers;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import iot.tetracubered.data.entities.Action;
import iot.tetracubered.data.entities.Device;
import iot.tetracubered.data.entities.Feature;
import iot.tetracubered.data.repositories.ActionRepository;
import iot.tetracubered.data.repositories.DeviceRepository;
import iot.tetracubered.data.repositories.FeatureRepository;
import iot.tetracubered.enumerations.FeatureType;
import iot.tetracubered.enumerations.RequestSourceType;
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
import java.util.UUID;

@ApplicationScoped
public class DeviceProvisioningConsumer {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceProvisioningConsumer.class);

    @Inject
    Jsonb jsonb;

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
        var storedDeviceUni = this.deviceRepository.existsByCircuitId(deviceProvisioningPayload.getId())
                .flatMap(deviceExists -> {
                    if (deviceExists) {
                        return Uni.createFrom().nullItem();
                    }

                    var device = new Device(
                            UUID.randomUUID(),
                            deviceProvisioningPayload.getId(),
                            deviceProvisioningPayload.getName(),
                            true,
                            deviceProvisioningPayload.getFeedbackTopic(),
                            null
                    );
                    return this.deviceRepository.storeDevice(device);
                });

        var storedDeviceAndFeatures = storedDeviceUni.onItem()
                .invoke(device -> {
                    this.storeFeatures(device, deviceProvisioningPayload.getFeatures());
                });

        storedDeviceAndFeatures.subscribe()
                .with(storedDevice -> {
                    if (storedDevice == null) {
                        LOGGER.info("Device is already stored");
                        return;
                    }

                    LOGGER.info("Stored device");
                });
       /* var deviceWithFeatures = deviceUni.flatMap(device -> this.storeFeature(device, deviceProvisioningPayload.getFeatures()))
                .flatMap(features -> deviceUni.map(device -> device.getFeatures().addAll(features)));
        deviceWithFeatures.subscribe().with(d ->);*/
    }

    private void storeFeatures(Device device, List<DeviceFeatureProvisioningPayload> features) {
        var storedFeatures = Multi.createFrom().items(features.stream())
                .flatMap(feature -> {
                    var newFeature = new Feature(
                            UUID.randomUUID(),
                            feature.getFeatureId(),
                            feature.getName(),
                            feature.getFeatureType(),
                            feature.getValue(),
                            device,
                            false,
                            null,
                            null,
                            null
                    );

                    return this.featureRepository.storeFeature(newFeature)
                            .toMulti();
                });

        storedFeatures.subscribe()
                .with(feature -> {
                    LOGGER.info("Stored feature");
                });
    }

    private Uni<Feature> storeAction(Uni<Feature> featureUni, List<DeviceFeatureActionProvisioningPayload> actionsToStore) {
        return featureUni.flatMap(feature ->
                Multi.createFrom().items(actionsToStore.stream())
                        .flatMap(actionToStore -> {
                            var action = new Action(
                                    UUID.randomUUID(),
                                    actionToStore.getActionId(),
                                    actionToStore.getTriggerTopic(),
                                    actionToStore.getName(),
                                    feature
                            );
                            return this.actionRepository.saveAction(action);
                        })
        );
    }
}
