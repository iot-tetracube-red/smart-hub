package iot.tetracube.data.repositories;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import iot.tetracube.data.entities.Device;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Query;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.UUID;

@ApplicationScoped
public class DeviceRepository extends BaseRepository {

    private final Driver driver;

    public DeviceRepository(Driver driver) {
        this.driver = driver;
    }

    public Uni<Boolean> deviceExistsByCircuitId(UUID circuitId) {
        var queryString = "match (device:Device {circuitId: $circuitId}) return count(device) = 1 as exists";
        var params = new HashMap<String, Object>();
        params.put("circuitId", circuitId.toString());
        var query = new Query(queryString, params);
        return this.executeQuery(this.driver, query)
                .map(rxResult -> rxResult.get("exists").asBoolean());
        //return Uni.createFrom().publisher(session.close());
    }

    public Uni<Device> getDeviceByCircuitId(UUID circuitId) {
        var queryString = "match (device:Device {circuitId: $circuitId}) return device";
        var params = new HashMap<String, Object>();
        params.put("circuitId", circuitId.toString());
        var query = new Query(queryString, params);
        return this.executeQuery(this.driver, query)
                .map(Device::new);
    }

    public Uni<Device> createDevice(Device device) {
        var queryString = "CREATE " +
                "(device:Device {id: $id, circuitId: $circuitId, name: $name, isOnline: $isOnline, alexaSlotId: $alexaSlotId, clientName: $clientName}) " +
                "RETURN device LIMIT 1";
        var params = new HashMap<String, Object>();
        params.put("id", device.getId().toString());
        params.put("circuitId", device.getCircuitId().toString());
        params.put("name", device.getName());
        params.put("isOnline", device.getOnline());
        params.put("alexaSlotId", "");
        params.put("clientName", device.getClientName());
        var query = new Query(queryString, params);
        return this.executeQuery(this.driver, query)
                .map(Device::new);
    }
}
