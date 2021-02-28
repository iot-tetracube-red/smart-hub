package iot.tetracubered.iot.consumers;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
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
        var checkCircuitIdUni = this.deviceRepository.existsByCircuitId(deviceProvisioningPayload.getId());
        var storedDeviceUni = checkCircuitIdUni.flatMap(deviceExists ->
                this.upsertDevice(deviceExists, deviceProvisioningPayload)
        );
        return storedDeviceUni.flatMap(device ->
                this.storeFeatures(device, deviceProvisioningPayload.getFeatures())
        );
    }

    private Uni<Device> upsertDevice(boolean deviceExists, DeviceProvisioningPayload deviceProvisioningPayload) {
        var dbDeviceToStore = Device.generateNewDevice(
                deviceProvisioningPayload.getId(),
                deviceProvisioningPayload.getName(),
                deviceProvisioningPayload.getFeedbackTopic()
        );

        if (deviceExists) {
            return this.deviceRepository.updateDevice(deviceProvisioningPayload.getId(), dbDeviceToStore);
        }

        return this.deviceRepository.storeDevice(dbDeviceToStore);
    }

    private Uni<Device> storeFeatures(Device device, List<DeviceFeatureProvisioningPayload> featuresPayload) {
        return Multi.createFrom().items(featuresPayload.parallelStream())
                .flatMap(feature ->
                        this.featureRepository.existsByFeatureId(feature.getFeatureId())
                                .map(featureExists -> Tuple2.of(feature, featureExists))
                                .toMulti()
                )
                .flatMap(featureTuple -> {
                    var feature = featureTuple.getItem1();
                    var newFeature = Feature.generateNewDevice(
                            feature.getFeatureId(),
                            feature.getName(),
                            feature.getFeatureType(),
                            feature.getValue(),
                            device
                    );

                    var dbFeatureMulti = featureTuple.getItem2()
                            ? this.featureRepository.updateFeature(newFeature)
                            .toMulti()
                            : this.featureRepository.storeFeature(newFeature)
                            .toMulti();
                    return dbFeatureMulti.map(dbFeature -> Tuple2.of(dbFeature, feature));
                })
                .flatMap(featureTuple ->
                        this.storeAction(
                                featureTuple.getItem1(),
                                featureTuple.getItem2().getActions()
                        )
                                .toMulti()
                )
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
