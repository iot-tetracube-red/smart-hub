package iot.tetracubered.data.repositories

import io.smallrye.mutiny.Uni
import io.vertx.mutiny.pgclient.PgPool
import io.vertx.mutiny.sqlclient.Tuple
import iot.tetracubered.data.entities.Device
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DeviceRepository(
    private val pgPool: PgPool
) {

    fun getDeviceById(id: UUID): Uni<Device?> {
        val query = """
            select *
            from devices
            where id = $1
        """
        val params = Tuple.of(id)
        return this.pgPool.preparedQuery(query)
            .execute(params)
            .map { rows -> rows.iterator() }
            .map { rowIterator -> if(rowIterator.hasNext()) rowIterator.next() else null }
            .map { row -> if (row != null) Device(row) else null }
    }

    fun storeDevice(device: Device): Uni<Device> {
        val query = """
            insert into devices (id, name, is_online, feedback_topic, color_code) 
            values ($1, $2, $3, $4, $5)
        """
        val params = Tuple.of(
            device.id,
            device.name,
            device.isOnline,
            device.feedbackTopic,
            device.colorCode
        )
        return this.pgPool.preparedQuery(query)
            .execute(params)
            .flatMap { _ -> this.getDeviceById(device.id).map { storedDevice -> storedDevice!! } }
    }

    fun updateDevice(device: Device): Uni<Device> {
        val query = """
            update devices set feedback_topic = $1, is_online = $2 where id = $3
        """
        val params = Tuple.of(
            device.feedbackTopic,
            device.isOnline,
            device.id
        )
        return this.pgPool.preparedQuery(query)
            .execute(params)
            .flatMap { _ -> this.getDeviceById(device.id).map { storedDevice -> storedDevice!! } }
    }
}