package iot.tetracubered.repositories

import io.vertx.mutiny.pgclient.PgPool
import javax.inject.Inject

class DeviceRepository(@Inject private val pgPool: PgPool) {

   /* fun deviceExistsByCircuitId(circuitId: UUID): Mono<Boolean> {
        val c = {
            this.pgPool.preparedQuery("select count(id) > 0 as exists from devices where device_id = :id")
                    .rxExecute(Tuple.of(circuitId))
                    .map { rows -> rows.iterator() }
                    .map { rowIterator ->
                        val row: Row? = if (rowIterator.hasNext())
                            rowIterator.next()
                        else
                            null

                        if (row == null)
                            false
                        else
                            row.getBoolean("exists")
                    }
                .ignoreElement()
        }
        return Mono.from(rxStream)
    }*/
}