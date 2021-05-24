package red.tetracube.smarthub.iot.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.graalvm.collections.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import red.tetracube.smarthub.data.entities.Action;
import red.tetracube.smarthub.data.entities.Device;
import red.tetracube.smarthub.data.entities.Feature;
import red.tetracube.smarthub.data.repositories.ActionRepository;
import red.tetracube.smarthub.data.repositories.DeviceRepository;
import red.tetracube.smarthub.data.repositories.FeatureRepository;
import red.tetracube.smarthub.iot.payloads.ActionProvisioning;
import red.tetracube.smarthub.iot.payloads.DeviceProvisioning;
import red.tetracube.smarthub.iot.payloads.FeatureProvisioning;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DeviceProvisioningService {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    DeviceRepository deviceRepository;

    @Inject
    FeatureRepository featureRepository;

    @Inject
    ActionRepository actionRepository;

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceProvisioningService.class);

    @ConsumeEvent("device-provisioning")
    public void consumeDeviceProvisioningMessage(byte[] message) {
        LOGGER.info("Arrived a device to register");
        DeviceProvisioning deviceProvisioning;
        try {
            deviceProvisioning = objectMapper.readValue(message, DeviceProvisioning.class);
        } catch (Exception ex) {
            LOGGER.info("Cannot deserialize message: ignoring it");
            return;
        }

        manageDeviceProvisioning(deviceProvisioning)
                .subscribe()
                .with(device -> {
                    if (device == null) {
                        return;
                    }
                    LOGGER.info("Device managed correctly with circuit id: {}", device.getId());
                });
    }

    private Uni<Device> manageDeviceProvisioning(DeviceProvisioning deviceProvisioning) {
        LOGGER.info("Insert or update device with id {}", deviceProvisioning.id());
        return deviceRepository.insertOrUpdateDevice(
                new Device(
                        deviceProvisioning.id(),
                        deviceProvisioning.name(),
                        deviceProvisioning.feedbackTopic(),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        )
                .map(optionalDevice -> {
                    if (optionalDevice.isEmpty()) {
                        LOGGER.warn("Something went wrong during device persisting into database, sending negative feedback to device");
                        return null;
                    }
                    return optionalDevice.get();
                })
                .call(device -> {
                    if (device != null) {
                        LOGGER.info("Proceeding to manage features");
                        return manageFeatureProvisioning(device, deviceProvisioning.features());
                    }
                    return Uni.createFrom().voidItem();
                });
    }

    private Uni<List<Feature>> manageFeatureProvisioning(Device device, List<FeatureProvisioning> features) {
        return Multi.createFrom().items(features.parallelStream())
                .flatMap(feature -> {
                    LOGGER.info("Insert or update feature with id {}", feature.featureId());
                    return featureRepository.insertOrUpdateFeature(
                            new Feature(
                                    feature.featureId(),
                                    feature.name(),
                                    feature.featureType(),
                                    false,
                                    null,
                                    null,
                                    device.getId()
                            )
                    )
                            .map(dbFeature -> Pair.create(dbFeature, feature))
                            .toMulti();
                })
                .invoke(optionalFeature -> {
                    if (optionalFeature.getLeft().isEmpty()) {
                        LOGGER.info("Something went wrong during feature persisting into database, sending negative feedback to device");
                    } else {
                        LOGGER.info("Feature with id {} is managed correctly", optionalFeature.getLeft().get().getId());
                    }
                })
                .filter(featurePair -> featurePair.getLeft().isPresent())
                .map(featurePair -> Pair.create(featurePair.getLeft().get(), featurePair.getRight()))
                .call(featurePair -> manageActionProvisioning(featurePair.getLeft(), featurePair.getRight().actions()))
                .map(Pair::getLeft)
                .collect().asList();
    }

    private Uni<List<Action>> manageActionProvisioning(Feature feature, List<ActionProvisioning> actions) {
        return Multi.createFrom().items(actions.parallelStream())
                .flatMap(action ->
                        actionRepository.insertOrUpdateAction(
                                new Action(
                                        action.id(),
                                        action.name(),
                                        action.triggerTopic(),
                                        feature.getId()
                                )
                        )
                                .toMulti()
                )
                .invoke(optionalAction -> {
                    if (optionalAction.isEmpty()) {
                        LOGGER.info("Something went wrong during action persisting into database, sending negative feedback to device");
                    } else {
                        LOGGER.info("Action id {} managed correctly", optionalAction.get().getId());
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect()
                .asList();
    }
}
