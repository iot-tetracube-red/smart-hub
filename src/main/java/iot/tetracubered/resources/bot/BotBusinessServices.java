package iot.tetracubered.resources.bot;

import io.smallrye.mutiny.Multi;
import iot.tetracubered.data.entities.Device;
import iot.tetracubered.data.repositories.DeviceRepository;
import iot.tetracubered.data.repositories.FeatureRepository;
import iot.tetracubered.resources.bot.payloads.GetFeaturesResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class BotBusinessServices {

    @Inject
    DeviceRepository deviceRepository;

    @Inject
    FeatureRepository featureRepository;

    public Multi<GetFeaturesResponse> getFeatures() {
        return this.deviceRepository.getDevices()
                .flatMap(this::collectFeaturePerDevice);
    }

    private Multi<GetFeaturesResponse> collectFeaturePerDevice(Device device) {
        return this.featureRepository.getDeviceFeatures(device.getId())
                .map(feature -> new GetFeaturesResponse(device.getName(), feature.getName()));
    }
}
