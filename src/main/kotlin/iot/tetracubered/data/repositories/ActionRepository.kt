package iot.tetracubered.data.repositories

import io.vertx.mutiny.pgclient.PgPool
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ActionRepository(private val pgPool: PgPool) {
}