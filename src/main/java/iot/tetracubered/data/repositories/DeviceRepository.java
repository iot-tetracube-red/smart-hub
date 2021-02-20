package iot.tetracubered.data.repositories;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny.Session;

import io.smallrye.mutiny.Multi;
import iot.tetracubered.data.entities.Device;

import java.util.UUID;

@ApplicationScoped
public class DeviceRepository {

    @Inject
    Session hibernateReactiveSession;

    public Multi<Device> getDevices() {
        var queryStatement = this.hibernateReactiveSession.createQuery("from devices", Device.class);
        return queryStatement.getResults();
    }

    public Uni<Device> getDeviceByName(String deviceName) {
        return this.hibernateReactiveSession.createQuery(
                "from devices where name = :deviceName",
                Device.class
        )
                .setParameter("deviceName", deviceName)
                .getSingleResultOrNull();
    }

    public Uni<Boolean> existsByCircuitId(UUID circuitId) {
        return this.hibernateReactiveSession.createNativeQuery("""
                select count(id) > 0 as exists
                from devices
                where circuit_id = :circuitId
                """,
                Boolean.class
        )
                .setParameter("circuitId", circuitId)
                .getSingleResult();
    }

    public Uni<Device> storeDevice(Device device) {
        return this.hibernateReactiveSession.persist(device)
                .chain(() -> this.getDeviceById(device.getCircuitId()));
    }

    public Uni<Device> getDeviceById(UUID circuitId) {
        return this.hibernateReactiveSession.createQuery(
                "from devices where circuit_id = :circuitId",
                Device.class
        )
                .setParameter("circuitId", circuitId)
                .getSingleResult();
    }
}
