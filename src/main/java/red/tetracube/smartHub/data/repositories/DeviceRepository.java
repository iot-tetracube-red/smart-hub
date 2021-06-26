package red.tetracube.smartHub.data.repositories;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import red.tetracube.smartHub.data.entities.Device;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DeviceRepository implements ReactivePanacheMongoRepository<Device> {
}
