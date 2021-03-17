package iot.tetracubered.data.repositories

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.mutiny.pgclient.PgPool
import io.vertx.mutiny.sqlclient.Tuple
import iot.tetracubered.data.entities.Device
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DeviceRepository(
    private val pgPool: PgPool
) {

    suspend fun getDeviceById(id: UUID): Device? {
        val query = """
            select *
            from devices
            where id = $1
        """
        val params = Tuple.of(id)
        return this.pgPool.preparedQuery(query)
            .execute(params)
            .map { rows -> rows.iterator() }
            .map { rowIterator -> if (rowIterator.hasNext()) rowIterator.next() else null }
            .map { row -> if (row != null) Device(row) else null }
            .awaitSuspending()
    }

    suspend fun storeDevice(device: Device): Device {
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
        this.pgPool.preparedQuery(query).execute(params).awaitSuspending()
        return this.getDeviceById(device.id)!!
    }

    suspend fun updateDevice(device: Device): Device {
        val query = """
            update devices set feedback_topic = $1, is_online = $2 where id = $3
        """
        val params = Tuple.of(
            device.feedbackTopic,
            device.isOnline,
            device.id
        )
        this.pgPool.preparedQuery(query).execute(params).awaitSuspending()
        return this.getDeviceById(device.id)!!
    }

    suspend fun getAllDevices(): List<Device> {
        val query = """
            select *
            from devices
        """
        return this.pgPool.preparedQuery(query).execute()
            .onItem()
            .transformToMulti { rows -> Multi.createFrom().iterable(rows) }
            .map { row -> Device(row) }
            .collect().asList()
            .awaitSuspending()
    }

    suspend fun getDeviceByName(name: String): Device? {
        val query = """
           select *
            from devices
            where name = $1
        """
        val params = Tuple.of(name)
        val rows = this.pgPool.preparedQuery(query).execute(params).awaitSuspending()
        val rowIterator = rows.iterator()
        if (rowIterator.hasNext()) {
            return Device(rowIterator.next())
        }
        return null
    }
}