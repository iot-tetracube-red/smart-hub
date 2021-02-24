package iot.tetracubered.data.repositories;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import iot.tetracubered.data.entities.Action;
import iot.tetracubered.data.entities.Feature;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class ActionRepository extends BaseRepository {

    public Uni<Action> storeAction(Action action) {
        var query = """
                insert into actions (id, action_id, trigger_topic, name, feature_id) 
                VALUES ($1, $2, $3, $4, $5)
                """;
        var parameters = Tuple.of(
                action.getId(),
                action.getActionId(),
                action.getTriggerTopic(),
                action.getName(),
                action.getFeature().getId()
        );
        return this.pgPool.preparedQuery(query).execute(parameters)
                .chain(() -> this.findActionByActionId(action.getActionId()));
    }

    public Uni<Action> findActionByActionId(UUID actionId) {
        var query = """
                """;
        var parameters = Tuple.of(actionId);
        return this.pgPool.preparedQuery(query).execute(parameters)
                .stage(rowSetUni -> {
                    var action = new Action();
                    return this.mapRowToObject(rowSetUni, action);
                });
    }
/*
    @Inject
    Session hibernateReactiveSession;


    public Multi<Action> getFeatureActions(UUID featureId) {
        return hibernateReactiveSession.createNativeQuery(
                "from actions where feature_id = :featureId",
                Action.class
        )
                .setParameter("featureId", featureId)
                .getResults();
    }



   */
}
