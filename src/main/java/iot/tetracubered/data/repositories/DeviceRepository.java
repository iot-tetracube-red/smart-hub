package iot.tetracubered.data.repositories;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import iot.tetracubered.data.entities.Device;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class DeviceRepository implements PanacheRepository<Device> {

    public Multi<Device> getDevices() {
        return this.listAll()
                .onItem()
                .transformToMulti(devices -> Multi.createFrom().items(devices.stream()));
    }

    public Uni<Boolean> existsByCircuitId(UUID circuitId) {
        return this.count("circuit_id", circuitId)
                .map(foundDevices -> foundDevices == 1);
    }

    public Uni<Device> getDeviceByCircuitId(UUID circuitId) {
        return this.find("circuit_id", circuitId)
                .firstResult();
    }

    public Uni<Device> getDeviceById(UUID id) {
        return this.find("id", id)
                .firstResult();
    }

    public Uni<Device> getDeviceByName(String name) {
        return this.find("name", name)
                .firstResult();
    }

    public Uni<Device> saveDevice(Device device) {
       return this.saveDevice(device);
    }
}
