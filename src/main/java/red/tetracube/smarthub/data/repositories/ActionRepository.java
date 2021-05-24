package red.tetracube.smarthub.data.repositories;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import red.tetracube.smarthub.annotations.processors.EntityProcessor;
import red.tetracube.smarthub.data.entities.Action;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ActionRepository {

    @Inject
    PgPool pgPool;

    @Inject
    EntityProcessor entityProcessor;

    public Uni<Optional<Action>> insertOrUpdateAction(Action action) {
        var query = """
                INSERT INTO actions(id, name, trigger_topic, feature_id)
                VALUES ($1, $2, $3, $4)
                ON CONFLICT (id) DO
                UPDATE SET trigger_topic = $3;
                """;
        var parameters = Tuple.of(
                action.getId(),
                action.getName(),
                action.getTriggerTopic(),
                action.getFeatureId()
        );
        return pgPool.preparedQuery(query)
                .execute(parameters)
                .flatMap(ignored -> getActionById(action.getId()));
    }

    public Uni<Optional<Action>> getActionById(UUID id) {
        return pgPool.preparedQuery("""
                select id, name, trigger_topic, feature_id
                from actions
                where id = $1
                """)
                .execute(Tuple.of(id))
                .map(rows -> {
                    var rowsIterator = rows.iterator();
                    if (!rowsIterator.hasNext()) {
                        return Optional.empty();
                    }
                    var row = rowsIterator.next();
                    try {
                        var featureEntity = entityProcessor.mapTableToEntity(new Action(), row);
                        return Optional.of(featureEntity);
                    } catch (IllegalAccessException e) {
                        return Optional.empty();
                    }
                });
    }
}
