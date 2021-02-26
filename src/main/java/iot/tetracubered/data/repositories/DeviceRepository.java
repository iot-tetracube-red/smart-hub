package iot.tetracubered.data.repositories;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.RowIterator;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import iot.tetracubered.data.entities.Device;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class DeviceRepository extends BaseRepository {

    public Uni<Boolean> existsByCircuitId(UUID circuitId) {
        var query = """
                select count(id) > 0 as exists
                from devices
                where circuit_id = $1
                """;
        var parameters = Tuple.of(circuitId);
        return this.pgPool.preparedQuery(query).execute(parameters)
                .map(RowSet::iterator)
                .map(RowIterator::next)
                .map(row -> row.getBoolean("exists"));
    }

    public Uni<Device> storeDevice(Device device) {
        var query = """
                insert into devices (id, circuit_id, name, is_online, feedback_topic, color_code) 
                VALUES($1, $2, $3, $4, $5, $6)
                """;
        var parameters = Tuple.of(
                device.getId(),
                device.getCircuitId(),
                device.getName(),
                device.getIsOnline(),
                device.getFeedbackTopic(),
                device.getColorCode()
        );
        return this.pgPool.preparedQuery(query).execute(parameters)
                .chain(() -> this.getDeviceByCircuitId(device.getCircuitId()));
    }

    public Uni<Device> updateDevice(UUID circuitId, Device device) {
        var query = """
                UPDATE devices SET is_online = $1, feedback_topic = $2
                WHERE circuit_id = $3
                """;
        var parameters = Tuple.of(
                device.getIsOnline(),
                device.getFeedbackTopic(),
                circuitId
        );
        return this.pgPool.preparedQuery(query).execute(parameters)
                .chain(() -> this.getDeviceByCircuitId(circuitId));
    }

    public Uni<Device> getDeviceByCircuitId(UUID circuitId) {
        var query = """
                select *
                from devices
                where circuit_id = $1
                """;
        var parameters = Tuple.of(circuitId);
        return this.pgPool.preparedQuery(query).execute(parameters)
                .stage(rowSetUni -> {
                    var device = new Device();
                    return this.mapRowToObject(rowSetUni, device);
                });
    }

   /* public Multi<Device> getDevices() {
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





*/
}
