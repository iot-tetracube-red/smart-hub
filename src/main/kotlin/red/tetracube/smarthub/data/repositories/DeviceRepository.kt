package red.tetracube.smarthub.data.repositories

import io.vertx.mutiny.pgclient.PgPool
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DeviceRepository(private val pgPool: PgPool) {

}
