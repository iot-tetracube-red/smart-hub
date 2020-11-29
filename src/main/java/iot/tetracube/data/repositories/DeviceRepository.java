package iot.tetracube.data.repositories;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import iot.tetracube.data.entities.Device;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class DeviceRepository {

    private final PgPool pgPool;

    public DeviceRepository(PgPool pgPool) {
        this.pgPool = pgPool;
    }

    public Multi<Device> getDevices() {
        var query = """
                SELECT id, name, circuit_id, is_online, alexa_slot_id, client_name
                FROM devices
                """;
        return this.pgPool.query(query).execute()
                .onItem()
                .transformToMulti(rows -> Multi.createFrom().iterable(rows))
                .onItem()
                .transform(Device::new);
    }

    public Uni<Boolean> deviceExistsByCircuitId(UUID circuitId) {
        var query = "SELECT count(id) = 1 as exists FROM devices WHERE circuit_id = $1";
        var parameters = Tuple.of(circuitId);
        return this.pgPool.preparedQuery(query).execute(parameters)
                .map(RowSet::iterator)
                .map(rowIterator ->
                        rowIterator.hasNext() ? rowIterator.next().getBoolean("exists") : null
                );
    }

    public Uni<Device> getDeviceByCircuitId(UUID circuitId) {
        var query = "SELECT id, name, circuit_id, is_online, alexa_slot_id FROM devices WHERE circuit_id = $1";
        var parameters = Tuple.of(circuitId);
        return this.pgPool.preparedQuery(query).execute(parameters)
                .map(RowSet::iterator)
                .map(rowIterator ->
                        rowIterator.hasNext() ? new Device(rowIterator.next()) : null
                );
    }

    public Uni<Device> saveDevice(Device device) {
        var query = "INSERT INTO devices (id, name, circuit_id, is_online, client_name) VALUES($1, $2, $3, $4, $5) RETURNING *";
        var parameters = Tuple.of(
                device.getId(),
                device.getName(),
                device.getCircuitId(),
                device.getOnline(),
                device.getClientName()
        );
        return this.pgPool.preparedQuery(query).execute(parameters)
                .map(RowSet::iterator)
                .map(rowIterator ->
                        rowIterator.hasNext() ? new Device(rowIterator.next()) : null
                );
    }

}
