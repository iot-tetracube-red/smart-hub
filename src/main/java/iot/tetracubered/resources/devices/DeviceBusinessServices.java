package iot.tetracubered.resources.devices;

import io.smallrye.mutiny.Multi;
import iot.tetracubered.data.repositories.DeviceRepository;
import iot.tetracubered.resources.devices.payloads.DeviceResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class DeviceBusinessServices {

    @Inject
    DeviceRepository deviceRepository;

    public Multi<DeviceResponse> getDevices() {
        return this.deviceRepository.getDevices()
                .map(device ->
                        new DeviceResponse(
                                device.getId(),
                                device.getName(),
                                device.getOnline(),
                                device.getColorCode()
                        )
                );
    }
}
