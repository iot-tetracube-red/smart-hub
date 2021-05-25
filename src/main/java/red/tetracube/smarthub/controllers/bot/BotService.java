package red.tetracube.smarthub.controllers.bot;

import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import red.tetracube.smarthub.controllers.bot.payloads.BotDeviceFeatureItem;
import red.tetracube.smarthub.controllers.bot.payloads.DeviceFeatureAction;
import red.tetracube.smarthub.data.entities.Action;
import red.tetracube.smarthub.data.repositories.ActionRepository;
import red.tetracube.smarthub.data.repositories.DeviceRepository;
import red.tetracube.smarthub.data.repositories.FeatureRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BotService {

    @Inject
    DeviceRepository deviceRepository;

    @Inject
    FeatureRepository featureRepository;

    @Inject
    ActionRepository actionRepository;

    private final static Logger LOGGER = LoggerFactory.getLogger(BotService.class);

    public Uni<List<BotDeviceFeatureItem>> getDeviceFeatures() {
        return deviceRepository.getDevices()
                .flatMap(device -> {
                            LOGGER.info("Found device \"{}\", getting its features", device.getName());
                            return featureRepository.getFeaturesByDevice(device.getId())
                                    .map(feature -> {
                                                LOGGER.info("Found feature \"{}\"", feature.getName());
                                                return new BotDeviceFeatureItem(
                                                        device.getName(),
                                                        feature.getName(),
                                                        device.getColorCode()
                                                );
                                            }
                                    );
                        }
                )
                .collect()
                .asList();
    }

    public Uni<Optional<DeviceFeatureAction>> getDeviceFeatureActions(String deviceName,
                                                                      String featureName) {
        LOGGER.info("Searching for device with name \"{}\"", deviceName);
        return deviceRepository.getDeviceByName(deviceName)
                .flatMap(optionalDevice -> {
                    if (optionalDevice.isEmpty()) {
                        LOGGER.warn("Not found, returning empty object");
                        return Uni.createFrom().item(Optional.empty());
                    }
                    LOGGER.info("Found device id \"{}\"", optionalDevice.get().getId());
                    LOGGER.info("Searching for related feature name \"{}\"", featureName);
                    return featureRepository.getFeatureByDeviceAndName(optionalDevice.get().getId(), featureName)
                            .flatMap(optionalFeature -> {
                                if (optionalFeature.isEmpty()) {
                                    LOGGER.warn("Not found, returning empty object");
                                    return Uni.createFrom().item(Optional.empty());
                                }
                                LOGGER.info("Found, now searching for the related actions");
                                var actionsNames = actionRepository.getFeatureActions(optionalFeature.get().getId())
                                        .map(Action::getName)
                                        .invoke(action -> LOGGER.info("Found action \"{}\"", action))
                                        .collect().asList();
                                var deviceFeatureActionsUni = actionsNames.map(actions -> new DeviceFeatureAction(
                                        optionalDevice.get().getName(),
                                        optionalFeature.get().getName(),
                                        actions
                                ));
                                return deviceFeatureActionsUni.map(Optional::of);
                            });
                });
    }
}
