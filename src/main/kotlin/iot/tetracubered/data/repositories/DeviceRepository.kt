package iot.tetracubered.data.repositories

import io.smallrye.mutiny.Uni
import io.vertx.mutiny.pgclient.PgPool
import io.vertx.mutiny.sqlclient.Tuple
import iot.tetracubered.data.entities.Device
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DeviceRepository(private val pgPool: PgPool) {

    fun deviceExistsByCircuitId(circuitId: UUID): Uni<Boolean> {
        val query = """
            select count(id) > 0 device_exists
             from devices
             where circuit_id = $1
        """
        val params = Tuple.of(circuitId)
        return this.pgPool.preparedQuery(query).execute(params)
            .map { rows ->
                val rowIterator = rows.iterator()
                if (rowIterator.hasNext()) {
                    rowIterator.next().getBoolean("device_exists")
                } else {
                    false
                }
            }
    }

    fun getDeviceByCircuitId(circuitId: UUID): Uni<Device> {
        val query = """
           select id, circuit_id, name, is_online, feedback_topic, querying_topic, status, alexa_slot_id
            from devices
            where circuit_id = $1
        """
        val params = Tuple.of(circuitId)
        return this.pgPool.preparedQuery(query).execute(params)
            .map { rows ->
                val rowIterator = rows.iterator()
                Device(rowIterator.next())
            }
    }

    fun createDevice(device: Device): Uni<Device> {
        val query = """
            insert into devices(id, circuit_id, name, feedback_topic, querying_topic)
            values ($1, $2, $3, $4, $5)
            returning *
        """
        val params = Tuple.of(device.id, device.circuitId, device.name, device.feedbackTopic, device.queryingTopic)
        return this.pgPool.preparedQuery(query).execute(params)
            .map { rows ->
                val rowIterator = rows.iterator()
                Device(rowIterator.next())
            }
    }
}