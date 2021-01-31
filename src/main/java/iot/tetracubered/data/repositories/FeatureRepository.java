package iot.tetracubered.data.repositories;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowIterator;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import iot.tetracubered.data.entities.Feature;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class FeatureRepository {

    @Inject
    PgPool pgPool;

    public Multi<Feature> getDeviceFeatures(UUID deviceId) {
        var query = """
                select *
                from features
                where device_id = $1
                """;
        var queryParameters = Tuple.of(deviceId);
        return this.pgPool.preparedQuery(query).execute(queryParameters)
                .onItem().transformToMulti(rows -> Multi.createFrom().iterable(rows))
                .map(Feature::new)
                .onFailure().invoke(Throwable::printStackTrace);
    }

    public Uni<Boolean> existsByDeviceAndFeatureId(UUID circuitId, UUID featureId) {
        var query = """
                select count(features.id) > 0 as exists
                from features
                inner join devices d on d.id = features.device_id
                where d.circuit_id = $1 and features.feature_id = $2
                """;
        var queryParameters = Tuple.of(circuitId, featureId);
        return this.pgPool.preparedQuery(query).execute(queryParameters)
                .map(RowSet::iterator)
                .map(rowRowIterator -> rowRowIterator.next().getBoolean("exists"));
    }

    public Uni<Feature> getFeatureByDeviceAndFeatureId(UUID circuitId, UUID featureId) {
        var query = """
                select *
                from features
                inner join devices d on d.id = features.device_id
                where d.circuit_id = $1 and features.feature_id = $2
                """;
        var queryParameters = Tuple.of(circuitId, featureId);
        return this.pgPool.preparedQuery(query).execute(queryParameters)
                .map(RowSet::iterator)
                .map(RowIterator::next)
                .map(Feature::new);
    }

    public Uni<Feature> saveFeature(Feature feature) {
        var query = """
                insert into features(id, feature_id, name, feature_type, current_value, device_id) 
                VALUES ($1, $2, $3, $4, $5, $6)
                returning *
                """;
        var queryParameters = Tuple.of(
                feature.getId(),
                feature.getFeatureId(),
                feature.getName(),
                feature.getFeatureType().toString(),
                feature.getCurrentValue(),
                feature.getDeviceId()
        );
        return this.pgPool.preparedQuery(query).execute(queryParameters)
                .map(RowSet::iterator)
                .map(RowIterator::next)
                .map(Feature::new);
    }
}
