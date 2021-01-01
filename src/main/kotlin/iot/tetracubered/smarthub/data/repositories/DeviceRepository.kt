package iot.tetracubered.smarthub.data.repositories

import iot.tetracubered.smarthub.data.entities.Device
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DeviceRepository : ReactiveMongoRepository<Device, UUID> {
}