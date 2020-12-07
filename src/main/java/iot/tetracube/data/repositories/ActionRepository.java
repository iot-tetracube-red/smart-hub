package iot.tetracube.data.repositories;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowIterator;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import iot.tetracube.data.entities.Action;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ActionRepository {

    private final PgPool pgPool;

    public ActionRepository(PgPool pgPool) {
        this.pgPool = pgPool;
    }

    public Uni<Optional<Action>> getActionByName(String name) {
        var query = """
                """;
        var parameters = Tuple.of(name);
        return this.pgPool.preparedQuery(query).execute(parameters)
                .map(RowSet::iterator)
                .map(rowRowIterator -> rowRowIterator.hasNext() ? rowRowIterator.next() : null)
                .map(row -> row != null ? new Action(row) : null)
                .map(action -> action != null ? Optional.of(action) : Optional.empty());
    }

    public Multi<Action> getDeviceActions(UUID deviceId) {
        var query = """
                select id, action_id, name, is_default, publish_topic, alexa_intent, device_id
                from actions
                where device_id = $1
                """;
        var params = Tuple.of(deviceId);
        return this.pgPool.preparedQuery(query).execute(params)
                .onItem()
                .transformToMulti(rows -> Multi.createFrom().iterable(rows))
                .onItem()
                .transform(Action::new);
    }

    public Uni<Boolean> existsActionByActionId(UUID actionId) {
        var query = """
                select count(id) = 1 as exists
                from actions
                where action_id = $1
                """;
        var params = Tuple.of(actionId);
        return this.pgPool.preparedQuery(query).execute(params)
                .map(RowSet::iterator)
                .map(RowIterator::next)
                .map(row -> row.getBoolean("exists"));
    }

    public Uni<Optional<Action>> createAction(Action action) {
        var query = """
                insert into actions(id, action_id, name, is_default, publish_topic, device_id) 
                values ($1, $2, $3, $4, $5, $6)
                returning *
                """;
        var params = Tuple.of(
                action.getId(),
                action.getActionId(),
                action.getName(),
                action.isDefault(),
                action.getPublishTopic(),
                action.getDeviceId()
        );
        return this.pgPool.preparedQuery(query).execute(params)
                .onFailure()
                .recoverWithNull()
                .map(rows -> rows == null ? null : rows.iterator())
                .map(rowIterator -> rowIterator == null ? null : rowIterator.next())
                .map(row -> row == null ? null : new Action(row))
                .map(createdAction -> createdAction == null ? Optional.empty() : Optional.of(createdAction));
    }
}
