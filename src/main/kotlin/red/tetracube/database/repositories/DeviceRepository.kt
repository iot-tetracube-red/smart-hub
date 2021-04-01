package red.tetracube.database.repositories

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository
import red.tetracube.database.entities.Device
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DeviceRepository : ReactivePanacheMongoRepository<Device> {
}