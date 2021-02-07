package iot.tetracubered.resources.bot;

import io.smallrye.mutiny.Multi;
import iot.tetracubered.data.entities.Device;
import iot.tetracubered.data.entities.Feature;
import iot.tetracubered.data.repositories.ActionRepository;
import iot.tetracubered.data.repositories.DeviceRepository;
import iot.tetracubered.data.repositories.FeatureRepository;
import iot.tetracubered.resources.bot.payloads.GetCommandsResponse;
import iot.tetracubered.resources.bot.payloads.GetFeaturesResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class BotBusinessServices {

    @Inject
    DeviceRepository deviceRepository;

    @Inject
    FeatureRepository featureRepository;

    @Inject
    ActionRepository actionRepository;

    public Multi<GetFeaturesResponse> getFeatures() {
        return this.deviceRepository.getDevices()
                .flatMap(this::collectFeaturePerDevice);
    }

    public Multi<GetCommandsResponse> getCommandsByDeviceAndFeature(String deviceName,
                                                                    String featureName) {
        return this.featureRepository.getFeatureByDeviceAndFeatureName(deviceName, featureName)
                .toMulti()
                .flatMap(feature -> {
                    if (feature == null) {
                        return Multi.createFrom().nothing();
                    }
                    return this.mapCommandsByFeature(feature);
                });
    }

    private Multi<GetFeaturesResponse> collectFeaturePerDevice(Device device) {
        return this.featureRepository.getDeviceFeatures(device.getId())
                .map(feature -> new GetFeaturesResponse(device.getName(), feature.getName()));
    }

    private Multi<GetCommandsResponse> mapCommandsByFeature(Feature feature) {
        return this.actionRepository.getDeviceActions(feature.getId())
                .map(action -> new GetCommandsResponse(feature.getName(), action.getName()))
                .flatMap(getCommandsResponse -> this.setDeviceName(feature.getDeviceId(), getCommandsResponse));
    }

    private Multi<GetCommandsResponse> setDeviceName(UUID deviceId, GetCommandsResponse getCommandsResponse) {
        return this.deviceRepository.getDeviceById(deviceId)
                .map(device -> {
                    getCommandsResponse.setDeviceName(device.getName());
                    return getCommandsResponse;
                })
                .toMulti();
    }
}
