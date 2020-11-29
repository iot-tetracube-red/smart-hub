package iot.tetracube.data.repositories;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import iot.tetracube.data.entities.Action;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class ActionRepository {

    private final PgPool pgPool;

    public ActionRepository(PgPool pgPool) {
        this.pgPool = pgPool;
    }

    public Multi<Action> getActionsByDevice(UUID deviceId) {
        var query = "SELECT id, device_id, translation_key, hardware_id, alexa_intent, topic FROM actions WHERE device_id = $1";
        var parameters = Tuple.of(deviceId);
        return this.pgPool.preparedQuery(query).execute(parameters)
                .onItem()
                .transformToMulti(rows -> Multi.createFrom().iterable(rows))
                .onItem()
                .transform(Action::new);
    }

    public Uni<Boolean> existsActionByHardwareId(UUID hardwareId) {
        var query = "SELECT count(id) = 1 AS exists FROM actions WHERE hardware_id = $1";
        var parameters = Tuple.of(hardwareId);
        return this.pgPool.preparedQuery(query).execute(parameters)
                .map(RowSet::iterator)
                .map(rowIterator ->
                        rowIterator.hasNext() ? rowIterator.next().getBoolean("exists") : null
                );
    }

    public Uni<Action> saveAction(Action action) {
        var query = "INSERT INTO actions (id, device_id, translation_key, hardware_id, topic) VALUES ($1, $2, $3, $4, $5) RETURNING *";
        var parameters = Tuple.of(
                action.getId(),
                action.getDeviceId(),
                action.getTranslationKey(),
                action.getHardwareId(),
                action.getTopic()
        );
        return this.pgPool.preparedQuery(query).execute(parameters)
                .map(RowSet::iterator)
                .map(rowIterator -> rowIterator.hasNext() ? new Action(rowIterator.next()) : null);
    }

}
