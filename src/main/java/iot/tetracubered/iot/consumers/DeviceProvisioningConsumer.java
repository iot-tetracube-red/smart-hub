package iot.tetracubered.iot.consumers;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple;
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
        return this.deviceRepository.getDeviceByCircuitId(deviceProvisioningPayload.getId())
                .flatMap(dbDevice -> {
                    if (dbDevice == null) {
                        var deviceToStore = new Device(
                                deviceProvisioningPayload.getId(),
                                deviceProvisioningPayload.getName(),
                                deviceProvisioningPayload.getFeedbackTopic()
                        );
                        return this.deviceRepository.storeDevice(deviceToStore);
                    }

                    dbDevice.setFeedbackTopic(deviceProvisioningPayload.getFeedbackTopic());
                    dbDevice.setOnline(true);
                    return this.deviceRepository.updateDevice(dbDevice);
                })
                .call(device -> this.storeFeatures(device, deviceProvisioningPayload.getFeatures()));
    }

    private Uni<List<Feature>> storeFeatures(Device device, List<DeviceFeatureProvisioningPayload> featuresPayload) {
        return Multi.createFrom().items(featuresPayload.parallelStream())
                .flatMap(feature ->
                        this.featureRepository.getFeatureByFeatureId(feature.getFeatureId(), device.getId())
                                .map(dbFeature -> Tuple2.of(feature, dbFeature))
                                .onFailure().recoverWithItem(Tuple2.of(feature, null))
                                .toMulti()
                )
                .flatMap(featureTuple -> {
                    var featureProvisioning = featureTuple.getItem1();
                    var dbFeature = featureTuple.getItem2();
                    if (featureTuple.getItem2() == null) {
                        var newFeature = new Feature(
                                featureProvisioning.getFeatureId(),
                                featureProvisioning.getName(),
                                featureProvisioning.getFeatureType(),
                                featureProvisioning.getValue(),
                                device
                        );
                        return this.featureRepository.storeFeature(newFeature)
                                .map(storedFeature -> Tuple2.of(storedFeature, featureProvisioning.getActions()))
                                .toMulti();
                    }

                    dbFeature.setFeatureType(featureProvisioning.getFeatureType());
                    dbFeature.setCurrentValue(featureProvisioning.getValue());
                    dbFeature.setRunning(false);
                    dbFeature.setSourceReferenceId(null);
                    dbFeature.setSourceType(null);
                    return this.featureRepository.updateFeature(dbFeature)
                            .map(storedFeature -> Tuple2.of(storedFeature, featureProvisioning.getActions()))
                            .toMulti();
                })
                .call(featureActionsTuple -> this.storeAction(featureActionsTuple.getItem1(), featureActionsTuple.getItem2()))
                .map(Tuple2::getItem1)
                .collect().asList();
    }

    private Uni<Feature> storeAction(Feature feature, List<DeviceFeatureActionProvisioningPayload> actionsToStore) {
        return Multi.createFrom().items(actionsToStore.parallelStream())
                .flatMap(actionToStore -> {
                    var action = new Action(
                            actionToStore.getActionId(),
                            actionToStore.getTriggerTopic(),
                            actionToStore.getName(),
                            feature
                    );
                    return this.actionRepository.storeAction(action).toMulti();
                })
                .collect().asList()
                .map(actions -> {
                    feature.setActions(actions);
                    return feature;
                });
    }

}
