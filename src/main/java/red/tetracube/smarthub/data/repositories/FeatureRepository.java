package red.tetracube.smarthub.data.repositories;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import red.tetracube.smarthub.annotations.processors.EntityProcessor;
import red.tetracube.smarthub.data.entities.Feature;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class FeatureRepository {

    @Inject
    PgPool pgPool;

    @Inject
    EntityProcessor entityProcessor;

    public Uni<Optional<Feature>> insertOrUpdateFeature(Feature feature) {
        var query = """
                INSERT INTO features (id, name, feature_type, is_running, source_type, running_reference_id, device_id)
                VALUES ($1, $2, $3, $4, $5, $6, $7)
                ON CONFLICT (id) DO
                UPDATE SET feature_type = $3, is_running = $4, source_type = $5, running_reference_id = $6;
                """;
        var parameters = Tuple.of(
                feature.getId(),
                feature.getName(),
                feature.getFeatureType().toString(),
                feature.isRunning(),
                feature.getSourceType() == null ? null : feature.getSourceType().toString()
        );
        parameters = parameters.addString(feature.getRunningReferenceId());
        parameters = parameters.addUUID(feature.getDeviceId());
        return pgPool.preparedQuery(query)
                .execute(parameters)
                .flatMap(ignored -> getDeviceById(feature.getId()));
    }

    public Uni<Optional<Feature>> getDeviceById(UUID id) {
        return pgPool.preparedQuery("""
                select id, name, feature_type, is_running, source_type, running_reference_id, device_id
                from features
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
                        var featureEntity = entityProcessor.mapTableToEntity(new Feature(), row);
                        return Optional.of(featureEntity);
                    } catch (IllegalAccessException e) {
                        return Optional.empty();
                    }
                });
    }
}
