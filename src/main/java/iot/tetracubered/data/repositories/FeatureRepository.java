package iot.tetracubered.data.repositories;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import iot.tetracubered.data.entities.Feature;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class FeatureRepository {

    @Inject
    Mutiny.Session sqlRxSession;

    public Uni<Feature> storeFeature(Feature feature) {
        return this.sqlRxSession.persist(feature)
                .call(() -> this.sqlRxSession.flush())
                .chain(() -> this.sqlRxSession.find(Feature.class, feature.getId()));
    }

    public Uni<Feature> updateFeature(Feature feature) {
        return this.sqlRxSession.merge(feature)
                .call(() -> this.sqlRxSession.flush())
                .chain(() -> this.sqlRxSession.find(Feature.class, feature.getId()));
    }

    public Uni<Feature> getFeatureByFeatureId(UUID featureId, UUID deviceId) {
        var query = """
                select * 
                from features
                where feature_id = :featureId and device_id = :deviceId
                """;
        return this.sqlRxSession.createNativeQuery(query, Feature.class)
                .setParameter("featureId", featureId)
                .setParameter("deviceId", deviceId)
                .getSingleResultOrNull()
                .call(() -> this.sqlRxSession.flush());
    }

    public Uni<Boolean> existsByFeatureId(UUID featureId) {
        var query = """
                select count("id") > 0 as exists
                from features
                where feature_id = :featureId
                """;
        return this.sqlRxSession.createNativeQuery(query, Boolean.class)
                .setParameter("featureId", featureId)
                .getSingleResultOrNull()
                .call(() -> this.sqlRxSession.flush());
    }

    public Multi<Feature> getFeaturesByDeviceId(UUID deviceId) {
        var query = """
                select *
                from features
                where device_id = :deviceId
                """;
        return this.sqlRxSession.createNativeQuery(query, Feature.class)
                .setParameter("deviceId", deviceId)
                .getResults()
                .call(() -> this.sqlRxSession.flush());
    }
/*
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
