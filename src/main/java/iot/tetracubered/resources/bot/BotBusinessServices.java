package iot.tetracubered.resources.bot;

import io.smallrye.mutiny.Multi;
import iot.tetracubered.data.repositories.DeviceRepository;
import iot.tetracubered.data.repositories.FeatureRepository;
import iot.tetracubered.resources.bot.payloads.GetFeaturesResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class BotBusinessServices {

    @Inject
    DeviceRepository deviceRepository;

    @Inject
    FeatureRepository featureRepository;

    @Transactional
    public Multi<GetFeaturesResponse> getFeatures() {
        return this.featureRepository.getAllFeatures()
                .flatMap(feature ->
                        this.deviceRepository.getDeviceById(feature.getDevice().getId())
                                .toMulti()
                                .map(device -> {
                                    var getFeaturesResponse = new GetFeaturesResponse();
                                    getFeaturesResponse.setFeatureName(feature.getName());
                                    getFeaturesResponse.setDeviceName(device.getName());
                                    return getFeaturesResponse;
                                })
                );
    }
}
