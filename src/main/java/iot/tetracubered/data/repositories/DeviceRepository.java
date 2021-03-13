package iot.tetracubered.data.repositories;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import iot.tetracubered.data.entities.Device;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class DeviceRepository {

    @Inject
    Mutiny.Session sqlRxSession;

    public Uni<Boolean> existsByCircuitId(UUID circuitId) {
        var query = """
                select count(id) > 0 as exists
                from devices
                where circuit_id = :circuitId
                """;
        return this.sqlRxSession.createNativeQuery(query, Boolean.class)
                .setParameter("circuitId", circuitId)
                .getSingleResultOrNull()
                .call(() -> this.sqlRxSession.flush());
    }

    public Uni<Device> storeDevice(Device device) {
        return this.sqlRxSession.persist(device)
                .call(() -> this.sqlRxSession.flush())
                .chain(() -> this.getDeviceByCircuitId(device.getCircuitId()));
    }

    public Uni<Device> updateDevice(Device device) {
        return this.sqlRxSession.merge(device)
                .call(() -> this.sqlRxSession.flush())
                .chain(() -> this.getDeviceByCircuitId(device.getCircuitId()));
    }

    public Uni<Device> getDeviceByCircuitId(UUID circuitId) {
        var query = """
                select *
                from devices
                where circuit_id = :circuitId
                """;
        return this.sqlRxSession.createNativeQuery(query, Device.class)
                .setParameter("circuitId", circuitId)
                .getSingleResultOrNull()
                .call(() -> this.sqlRxSession.flush());
    }

    public Multi<Device> getDevices() {
        var query = """
                select *
                from devices
                """;
        return this.sqlRxSession.createNativeQuery(query, Device.class)
                .getResults()
                .call(() -> this.sqlRxSession.flush());
    }

    public Uni<Device> getDeviceByName(String deviceName) {
        var query = """
                select *
                from devices
                where name = :deviceName
                """;
        return this.sqlRxSession.createNativeQuery(query, Device.class)
                .setParameter("deviceName", deviceName)
                .getSingleResultOrNull()
                .call(() -> this.sqlRxSession.flush());
    }
}
