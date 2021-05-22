package red.tetracube.smarthub.data.repositories;

import io.smallrye.mutiny.Uni
import io.vertx.mutiny.pgclient.PgPool
import io.vertx.mutiny.sqlclient.Tuple
import red.tetracube.smarthub.data.entities.BrokerUser
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class BrokerUserRepository(private val pgPool: PgPool) {

    fun createUser(brokerUser: BrokerUser): Uni<Void> {
        return pgPool.preparedQuery(
            """
               insert into  mqtt_user(is_superuser, client_id, username, password, salt) 
               values ($1, $2, $3, $4, $5)
            """
        )
            .execute(
                Tuple.of(
                    brokerUser.isSuperUser,
                    brokerUser.clientId,
                    brokerUser.username,
                    brokerUser.password,
                    brokerUser.salt
                )
            )
            .flatMap { Uni.createFrom().voidItem() }
    }

    fun userExists(
        clientId: String,
        username: String
    ): Uni<Boolean> {
        return pgPool.preparedQuery(
            """
                        select (count(id) > 0) as exists
                        from mqtt_user
                        where username = $1 and client_id = $2
                        """
        )
            .execute(Tuple.of(username, clientId))
            .map { rowSet ->
                val rowIterator = rowSet.iterator()
                val row = rowIterator.next()
                row.getBoolean("exists")
            };
    }
}
