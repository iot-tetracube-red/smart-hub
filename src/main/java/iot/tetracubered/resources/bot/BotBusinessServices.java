package iot.tetracubered.resources.bot;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import iot.tetracubered.data.entities.Action;
import iot.tetracubered.data.repositories.ActionRepository;
import iot.tetracubered.data.repositories.DeviceRepository;
import iot.tetracubered.data.repositories.FeatureRepository;
import iot.tetracubered.resources.bot.payloads.GetCommandsResponse;
import iot.tetracubered.resources.bot.payloads.GetDeviceFeatureResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
public class BotBusinessServices {

    private final static Logger LOGGER = LoggerFactory.getLogger(BotBusinessServices.class);

    @Inject
    DeviceRepository deviceRepository;

    @Inject
    FeatureRepository featureRepository;

    @Inject
    ActionRepository actionRepository;
/*
    public Multi<GetDeviceFeatureResponse> getUserDevicesAndFeatures() {
        return this.deviceRepository.getDevices()
                .flatMap(device ->
                        this.featureRepository.getFeaturesByDeviceId(device.getId())
                                .map(feature ->
                                        new GetDeviceFeatureResponse(
                                                device.getName(),
                                                feature.getName()
                                        )
                                )
                );
    }

    public Uni<GetCommandsResponse> getCommandsForFeature(String deviceName, String featureName) {
        final var deviceId = new AtomicReference<UUID>();
        final var featureId = new AtomicReference<UUID>();

        LOGGER.info("Compose the response adding device name");
        var responseDeviceUni = this.deviceRepository.getDeviceByName(deviceName)
                .map(device -> {
                    if (device == null) {
                        return null;
                    }
                    deviceId.set(device.getId());
                    var response = new GetCommandsResponse();
                    response.setDeviceName(device.getName());
                    return response;
                });

        LOGGER.info("Adding feature to the response");
        var responseFeatureUni = responseDeviceUni.flatMap(response -> {
            if (response == null) {
                return Uni.createFrom().nullItem();
            }
            return this.featureRepository.getFeatureByDeviceAndName(deviceId.get(), response.getDeviceName())
                    .map(feature -> {
                        featureId.set(feature.getId());
                        response.setFeatureName(feature.getName());
                        return response;
                    });
        });

        LOGGER.info("Adding the actions to the response");
        return responseFeatureUni.flatMap(response -> {
            if (response == null) {
                return Uni.createFrom().nullItem();
            }
            return this.actionRepository.getFeatureActions(featureId.get())
                    .map(Action::getName)
                    .collect()
                    .asList()
                    .map(actionsName -> {
                        response.getCommands().addAll(actionsName);
                        return response;
                    });
        });
    }*/
}
