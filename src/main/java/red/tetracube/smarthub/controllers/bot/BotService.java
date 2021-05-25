package red.tetracube.smarthub.controllers.bot;

import io.smallrye.mutiny.Uni;
import red.tetracube.smarthub.controllers.bot.payloads.BotDeviceFeatureItem;
import red.tetracube.smarthub.controllers.bot.payloads.DeviceFeatureAction;
import red.tetracube.smarthub.data.entities.Action;
import red.tetracube.smarthub.data.entities.Feature;
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

    public Uni<List<BotDeviceFeatureItem>> getDeviceFeatures() {
        return deviceRepository.getDevices()
                .flatMap(device ->
                        featureRepository.getFeaturesByDevice(device.getId())
                                .map(feature ->
                                        new BotDeviceFeatureItem(
                                                device.getName(),
                                                feature.getName(),
                                                device.getColorCode()
                                        )
                                )
                )
                .collect()
                .asList();
    }

    public Uni<Optional<DeviceFeatureAction>> getDeviceFeatureActions(String deviceName,
                                                                      String featureName) {
        return deviceRepository.getDeviceByName(deviceName)
                .flatMap(optionalDevice -> {
                    if (optionalDevice.isEmpty()) {
                        return Uni.createFrom().item(Optional.empty());
                    }
                    return featureRepository.getFeatureByDeviceAndName(optionalDevice.get().getId(), featureName)
                            .flatMap(optionalFeature -> {
                                if (optionalFeature.isEmpty()) {
                                    return Uni.createFrom().item(Optional.empty());
                                }
                                var actionsNames = actionRepository.getFeatureActions(optionalFeature.get().getId())
                                        .map(Action::getName)
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
