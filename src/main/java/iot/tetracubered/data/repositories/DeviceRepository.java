package iot.tetracubered.data.repositories;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowIterator;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import iot.tetracubered.data.entities.Device;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class DeviceRepository {

    @Inject
    PgPool pgPool;

    public Multi<Device> getDevices() {
        final var query = """
                select *
                from devices
                 """;
        return this.pgPool.query(query).execute()
                .onItem()
                .transformToMulti(rowRowIterator -> Multi.createFrom().iterable(rowRowIterator))
                .onItem()
                .transform(Device::new);
    }

    public Uni<Boolean> existsByCircuitId(UUID circuitId) {
        var query = """
                select count(circuit_id) > 0 as exists
                from devices
                where circuit_id = $1
                """;
        var params = Tuple.of(circuitId);
        return this.pgPool.preparedQuery(query).execute(params)
                .map(RowSet::iterator)
                .map(rowRowIterator -> rowRowIterator.next().getBoolean("exists"));
    }

    public Uni<Device> getDeviceByCircuitId(UUID circuitId) {
        var query = """
                select *
                from devices
                where circuit_id = $1
                """;
        var params = Tuple.of(circuitId);
        return this.pgPool.preparedQuery(query).execute(params)
                .map(RowSet::iterator)
                .map(RowIterator::next)
                .map(Device::new);
    }

    public Uni<Device> getDeviceById(UUID id) {
        var query = """
                select *
                from devices
                where id = $1
                """;
        var params = Tuple.of(id);
        return this.pgPool.preparedQuery(query).execute(params)
                .map(RowSet::iterator)
                .map(RowIterator::next)
                .map(Device::new);
    }

    public Uni<Device> getDeviceByName(String name) {
        var query = """
                select *
                from devices
                where name = $1
                """;
        var params = Tuple.of(name);
        return this.pgPool.preparedQuery(query).execute(params)
                .map(RowSet::iterator)
                .map(rowRowIterator ->
                        rowRowIterator.hasNext()
                                ? new Device(rowRowIterator.next())
                                : null
                );
    }

    public Uni<Device> saveDevice(Device device) {
        var query = """
                insert into devices (id, circuit_id, name, feedback_topic, alexa_slot_id, color_code) 
                VALUES ($1, $2, $3, $4, null, null)
                returning *
                """;
        var params = Tuple.of(
                device.getId(),
                device.getCircuitId(),
                device.getName(),
                device.getFeedbackTopic()
        );
        return this.pgPool.preparedQuery(query).execute(params)
                .map(RowSet::iterator)
                .map(rowRowIterator -> rowRowIterator.hasNext() ? new Device(rowRowIterator.next()) : null);
    }
}
