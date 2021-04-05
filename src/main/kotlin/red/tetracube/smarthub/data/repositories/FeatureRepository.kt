package red.tetracube.smarthub.data.repositories

import io.vertx.mutiny.pgclient.PgPool
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FeatureRepository(private val pgPool: PgPool) {
}