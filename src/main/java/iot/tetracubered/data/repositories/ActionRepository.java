package iot.tetracubered.data.repositories;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowIterator;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import iot.tetracubered.data.entities.Action;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class ActionRepository {

    @Inject
    PgPool pgPool;

    public Multi<Action> getDeviceActions(UUID featureId) {
        var query = """
                select *
                from actions
                where feature_id = $1
                """;
        var queryParameters = Tuple.of(featureId);
        return this.pgPool.preparedQuery(query).execute(queryParameters)
                .onItem().transformToMulti(rows -> Multi.createFrom().iterable(rows))
                .map(Action::new)
                .onFailure().invoke(Throwable::printStackTrace);
    }

    public Uni<Boolean> actionExistsByFeatureAndActionId(UUID featureId, UUID actionId) {
        var query = """
                select count(actions.action_id) > 0 as exists
                from actions
                inner join features f on f.id = actions.feature_id
                where f.feature_id = $1 and actions.action_id = $2
                """;
        var queryParameters = Tuple.of(featureId, actionId);
        return this.pgPool.preparedQuery(query).execute(queryParameters)
                .map(RowSet::iterator)
                .map(RowIterator::next)
                .map(row -> row.getBoolean("exists"));
    }

    public Uni<Action> getActionByFeatureAndActionId(UUID featureId, UUID actionId) {
        var query = """
                select *
                from actions
                inner join features f on f.id = actions.feature_id
                where f.feature_id = $1 and actions.action_id = $2
                """;
        var queryParameters = Tuple.of(featureId, actionId);
        return this.pgPool.preparedQuery(query).execute(queryParameters)
                .map(RowSet::iterator)
                .map(RowIterator::next)
                .map(Action::new);
    }

    public Uni<Action> saveAction(Action action) {
        var query = """
                insert into actions(id, action_id, trigger_topic, name, alexa_intent, feature_id) 
                values ($1, $2, $3, $4, null, $5)
                returning *
                """;
        var queryParameters = Tuple.of(
                action.getId(),
                action.getActionId(),
                action.getTriggerTopic(),
                action.getName(),
                action.getFeatureId()
        );
        return this.pgPool.preparedQuery(query).execute(queryParameters)
                .map(RowSet::iterator)
                .map(RowIterator::next)
                .map(Action::new);
    }

    public Uni<String> getActionTriggerTopicByName(String deviceName, String featureName, String actionName) {
        var query = """
                select *
                from actions a
                inner join features f on f.id = a.feature_id
                inner join devices d on d.id = f.device_id
                where d.name = $1 and f.name = $2 and a.name = $3
                """;
        var queryParameters = Tuple.of(
                deviceName,
                featureName,
                actionName
        );
        return this.pgPool.preparedQuery(query).execute(queryParameters)
                .map(RowSet::iterator)
                .map(rowRowIterator ->
                        rowRowIterator.hasNext()
                                ? rowRowIterator.next().getString("trigger_topic")
                                : null
                );
    }
}
