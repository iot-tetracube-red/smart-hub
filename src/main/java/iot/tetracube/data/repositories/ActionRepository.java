package iot.tetracube.data.repositories;

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
        var query = "INSERT INTO actions (id, device_id, translation_key, hardware_id) VALUES ($1, $2, $3, $4) RETURNING *";
        var parameters = Tuple.of(action.getId(), action.getDeviceId(), action.getTranslationKey(), action.getHardwareId());
        return this.pgPool.preparedQuery(query).execute(parameters)
                .map(RowSet::iterator)
                .map(rowIterator ->
                        rowIterator.hasNext() ? new Action(rowIterator.next()) : null
                );
    }

}
