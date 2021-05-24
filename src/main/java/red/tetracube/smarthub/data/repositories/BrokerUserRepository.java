package red.tetracube.smarthub.data.repositories;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import red.tetracube.smarthub.data.entities.BrokerUser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class BrokerUserRepository {

    @Inject
    PgPool pgPool;

    public Uni<Void> createUser(BrokerUser brokerUser) {
        return pgPool.preparedQuery("""
                   insert into  mqtt_user(is_superuser, client_id, username, password, salt)
                   values ($1, $2, $3, $4, $5)
                """)
                .execute(
                        Tuple.of(
                                brokerUser.isSuperUser(),
                                brokerUser.getClientId(),
                                brokerUser.getUsername(),
                                brokerUser.getPassword(),
                                brokerUser.getSalt()
                        )
                )
                .map(ignored -> null);
    }

    public Uni<Boolean> userExists(String clientId,
                                   String username) {
        return pgPool.preparedQuery("""
                select (count(id) > 0) as exists
                from mqtt_user
                where username = $1 and client_id = $2
                """)
                .execute(Tuple.of(username, clientId))
                .map(rowSet -> {
                    var rowIterator = rowSet.iterator();
                    var row = rowIterator.next();
                    return row.getBoolean("exists");
                });
    }
}
