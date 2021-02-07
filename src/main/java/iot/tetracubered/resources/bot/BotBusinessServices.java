package iot.tetracubered.resources.bot;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import iot.tetracubered.data.entities.Action;
import iot.tetracubered.data.entities.Device;
import iot.tetracubered.data.entities.Feature;
import iot.tetracubered.data.repositories.ActionRepository;
import iot.tetracubered.data.repositories.DeviceRepository;
import iot.tetracubered.data.repositories.FeatureRepository;
import iot.tetracubered.resources.bot.payloads.GetCommandsResponse;
import iot.tetracubered.resources.bot.payloads.GetFeaturesResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.UUID;

@ApplicationScoped
public class BotBusinessServices {

    @Inject
    DeviceRepository deviceRepository;

    @Inject
    FeatureRepository featureRepository;

    @Inject
    ActionRepository actionRepository;

    @Inject
    EventBus eventBus;

    public Multi<GetFeaturesResponse> getFeatures() {
        return this.deviceRepository.getDevices()
                .flatMap(this::collectFeaturePerDevice);
    }

    public Uni<GetCommandsResponse> getCommandsByDeviceAndFeature(String deviceName,
                                                                    String featureName) {
        return this.deviceRepository.getDeviceByName(deviceName)
                .flatMap(device -> {
                    if (device == null) {
                        return Uni.createFrom().nullItem();
                    }

                    return this.mapCommandsByDevice(device, featureName);
                });
    }

    public Uni<Boolean> runDeviceFeatureAction(String deviceName, String featureName, String actionName) {
        return this.actionRepository.getActionTriggerTopicByName(deviceName, featureName, actionName)
                .flatMap(triggerTopic -> {
                    if (triggerTopic == null) {
                        return Uni.createFrom().nullItem();
                    }
                    // ToDo before run action, check if the device is online
                    return this.eventBus.<Boolean>request("query-action", triggerTopic)
                            .map(Message::body);
                });
    }

    private Multi<GetFeaturesResponse> collectFeaturePerDevice(Device device) {
        return this.featureRepository.getDeviceFeatures(device.getId())
                .map(feature -> new GetFeaturesResponse(device.getName(), feature.getName()));
    }

    private Uni<GetCommandsResponse> mapCommandsByDevice(Device device, String featureName) {
        return this.featureRepository.getFeatureByDeviceAndFeatureName(device.getId(), featureName)
                .flatMap(feature -> {
                    if (feature == null) {
                        return Uni.createFrom().nullItem();
                    }
                    var getCommandsResponse = new GetCommandsResponse(feature.getName(), featureName);
                    return this.actionRepository.getDeviceActions(feature.getId())
                            .map(Action::getName)
                            .collectItems().asList()
                            .map(actions -> {
                                getCommandsResponse.getCommands().addAll(actions);
                                return getCommandsResponse;
                            });
                });
    }
}
