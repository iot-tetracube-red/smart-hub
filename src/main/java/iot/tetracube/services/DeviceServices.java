package iot.tetracube.services;

import io.smallrye.mutiny.Multi;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import iot.tetracube.data.repositories.DeviceRepository;
import iot.tetracube.models.payloads.DeviceResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import java.util.ArrayList;

@ApplicationScoped
public class DeviceServices {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceServices.class);

    private final Jsonb jsonb;
    private final DeviceRepository deviceRepository;
    private final ActionServices actionServices;

    public DeviceServices(Jsonb jsonb,
                          DeviceRepository deviceRepository,
                          ActionServices actionServices) {
        this.jsonb = jsonb;
        this.deviceRepository = deviceRepository;
        this.actionServices = actionServices;
    }



    public Multi<DeviceResponse> getDevices() {
        return this.deviceRepository.getDevices()
                .map(device ->
                        new DeviceResponse(
                                device.getId(),
                                device.getName(),
                                new ArrayList<>()
                        )
                )
                .onItem()
                .call(device ->
                        this.actionServices.getDeviceActions(device.getId())
                                .collectItems().asList()
                                .onItem()
                                .invoke(actions -> device.getActions().addAll(actions))
                );
    }

}
