package red.tetracube.database.repositories

import io.quarkus.mongodb.panache.kotlin.PanacheMongoRepository
import red.tetracube.database.entities.Device
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DeviceRepository : PanacheMongoRepository<Device> {
}