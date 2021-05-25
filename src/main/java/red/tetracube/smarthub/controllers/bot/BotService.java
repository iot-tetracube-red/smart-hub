package red.tetracube.smarthub.controllers.bot;

import io.smallrye.mutiny.Uni;
import red.tetracube.smarthub.controllers.bot.payloads.BotDeviceFeatureItem;
import red.tetracube.smarthub.data.repositories.DeviceRepository;
import red.tetracube.smarthub.data.repositories.FeatureRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class BotService {

    @Inject
    DeviceRepository deviceRepository;

    @Inject
    FeatureRepository featureRepository;

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
}
