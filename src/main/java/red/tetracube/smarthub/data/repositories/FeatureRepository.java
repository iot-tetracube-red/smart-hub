package red.tetracube.smarthub.data.repositories;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import red.tetracube.smarthub.annotations.processors.EntityProcessor;
import red.tetracube.smarthub.data.entities.Feature;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

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
                .flatMap(ignored -> getFeatureById(feature.getId()));
    }

    public Uni<Optional<Feature>> getFeatureById(UUID id) {
        return pgPool.preparedQuery("""
                select id, name, feature_type, is_running, source_type, running_reference_id, device_id
                from features
                where id = $1
                """)
                .execute(Tuple.of(id))
                .map(this::mapSingleRow);
    }

    public Multi<Feature> getFeaturesByDevice(UUID deviceId) {
        return pgPool.preparedQuery("""
                select id, name, feature_type, is_running, source_type, running_reference_id, device_id
                from features
                where device_id = $1
                """)
                .execute(Tuple.of(deviceId))
                .onItem()
                .transformToMulti(rows -> Multi.createFrom().items(() -> StreamSupport.stream(rows.spliterator(), false)))
                .map(row -> {
                    try {
                        return entityProcessor.mapTableToEntity(new Feature(), row);
                    } catch (IllegalAccessException e) {
                        return null;
                    }
                });
    }

    public Uni<Optional<Feature>> getFeatureByDeviceAndName(UUID deviceId, String featureName) {
        return pgPool.preparedQuery("""
                select id, name, feature_type, is_running, source_type, running_reference_id, device_id
                from features
                where device_id = $1 and name = $2
                """)
                .execute(Tuple.of(deviceId, featureName))
                .map(this::mapSingleRow);
    }

    private Optional<Feature> mapSingleRow(RowSet<Row> rows) {
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
    }
}
