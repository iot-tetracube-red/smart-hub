package iot.tetracubered.resources.features;

import io.smallrye.mutiny.Multi;
import iot.tetracubered.data.repositories.ActionRepository;
import iot.tetracubered.data.repositories.FeatureRepository;
import iot.tetracubered.resources.features.payloads.DeviceFeatureResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class FeatureBusinessServices {

    @Inject
    FeatureRepository featureRepository;

    @Inject
    ActionRepository actionRepository;

    public Multi<DeviceFeatureResponse> getDeviceFeatures(UUID deviceId) {
        return this.featureRepository.getDeviceFeatures(deviceId)
                .map(feature ->
                        new DeviceFeatureResponse(
                                feature.getId(),
                                feature.getName(),
                                feature.getFeatureType(),
                                feature.getCurrentValue()
                        )
                );
    }
}
