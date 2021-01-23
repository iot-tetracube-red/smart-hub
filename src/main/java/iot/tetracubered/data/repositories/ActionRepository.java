package iot.tetracubered.data.repositories;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import iot.tetracubered.data.entities.Action;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class ActionRepository {

    @Inject
    PgPool pgPool;

    public Multi<Action> getDeviceActions(UUID actionId) {
        final var query = """
                select *
                from actions
                where device_id = $1
                """;
        final var queryParameters = Tuple.of(actionId);
        return this.pgPool.preparedQuery(query).execute(queryParameters)
                .onItem().transformToMulti(rows -> Multi.createFrom().iterable(rows))
                .map(row -> {
                    return new Action(row);
                })

                .onFailure().invoke(error -> {
                    error.printStackTrace();
                });
    }
}
