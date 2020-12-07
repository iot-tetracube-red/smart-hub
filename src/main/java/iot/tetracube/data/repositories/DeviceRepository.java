package iot.tetracube.data.repositories;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowIterator;
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

    public Uni<Boolean> deviceExistsByCircuitId(UUID circuitId) {
        var query = """
                select count(id) = 1 device_exists
                from devices
                where circuit_id = $1
                """;
        var params = Tuple.of(circuitId);
        return this.pgPool.preparedQuery(query).execute(params)
                .map(RowSet::iterator)
                .map(RowIterator::next)
                .map(row -> row.getBoolean("device_exists"));
    }

    public Uni<Device> getDeviceByCircuitId(UUID circuitId) {
        var query = """
                select id, circuit_id, name, is_online, alexa_slot_id, client_name
                from devices
                where circuit_id = $1
                """;
        var params = Tuple.of(circuitId);
        return this.pgPool.preparedQuery(query).execute(params)
                .map(RowSet::iterator)
                .map(RowIterator::next)
                .map(Device::new);
    }

    public Uni<Device> createDevice(Device device) {
        var query = """
                insert into devices(id, circuit_id, name, is_online, alexa_slot_id, client_name) 
                values ($1, $2, $3, $4, null, $5)
                returning *
                """;
        var params = Tuple.of(
                device.getId(),
                device.getCircuitId(),
                device.getName(),
                device.getOnline(),
                device.getClientName()
        );
        return this.pgPool.preparedQuery(query).execute(params)
                .map(RowSet::iterator)
                .map(RowIterator::next)
                .map(Device::new);
    }
}
