package iot.tetracubered.data.repositories;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.RowIterator;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import iot.tetracubered.data.entities.Feature;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class FeatureRepository extends BaseRepository {

    public Uni<Feature> storeFeature(Feature feature) {
        var query = """
                insert into features(id, feature_id, name, feature_type, current_value, device_id) 
                values ($1, $2, $3, $4, $5, $6)
                """;
        var parameters = Tuple.of(
                feature.getId(),
                feature.getFeatureId(),
                feature.getName(),
                feature.getFeatureType().toString(),
                feature.getCurrentValue(),
                feature.getDevice().getId()
        );
        return this.pgPool.preparedQuery(query).execute(parameters)
                .chain(() -> this.getFeatureById(feature.getFeatureId()));
    }

    public Uni<Feature> updateFeature( Feature feature) {
        var query = """
                update features set
                feature_type = $1,
                current_value = $2
                where feature_id = $3
                """;
        var parameters = Tuple.of(
                feature.getFeatureType().toString(),
                feature.getCurrentValue(),
                feature.getId()
        );
        return this.pgPool.preparedQuery(query).execute(parameters)
                .chain(() -> this.getFeatureById(feature.getFeatureId()));
    }

    public Uni<Feature> getFeatureById(UUID featureId) {
        var query = """
                select * 
                from features
                where feature_id = $1
                """;
        var parameters = Tuple.of(featureId);
        return this.pgPool.preparedQuery(query).execute(parameters)
                .stage(rowSetUni -> {
                    var feature = new Feature();
                    return this.mapRowToObject(rowSetUni, feature);
                });
    }

    public Uni<Boolean> existsByFeatureId(UUID featureId) {
        var query = """
                select count("id") > 0 as exists
                from features
                where feature_id = $1
                """;
        var parameters = Tuple.of(featureId);
        return this.pgPool.preparedQuery(query).execute(parameters)
                .map(RowSet::iterator)
                .map(RowIterator::next)
                .map(row -> row.getBoolean("exists"));
    }
/*
    public Multi<Feature> getFeaturesByDeviceId(UUID deviceId) {
        var query = """
                select *
                from features
                where device_id = $1
                """;
        var queryParameters = Tuple.of(deviceId);
        return this.pgPool.preparedQuery(query).execute(queryParameters)
                .onItem()
                .transformToMulti(rows -> Multi.createFrom().iterable(rows))
                .map(Feature::new);
    }

    public Uni<Feature> getFeatureByDeviceAndName(UUID deviceId, String featureName) {
        var query = """
                select *
                from features 
                where device_id = $1 and name = $2
                """;
        var queryParameters = Tuple.of(deviceId, featureName);
        return this.pgPool.preparedQuery(query).execute(queryParameters)
                .map(RowSet::iterator)
                .map(rowRowIterator -> rowRowIterator.hasNext() ? rowRowIterator.next() : null)
                .map(row -> row != null ? new Feature(row) : null);
    }



    */
}
