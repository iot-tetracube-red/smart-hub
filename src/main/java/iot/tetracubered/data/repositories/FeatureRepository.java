package iot.tetracubered.data.repositories;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny.Session;

import io.smallrye.mutiny.Multi;
import iot.tetracubered.data.entities.Feature;

@ApplicationScoped
public class FeatureRepository {

    @Inject
    Session hibernateReactiveSession;

    public Multi<Feature> getFeaturesByDeviceId(UUID deviceId) {
        var query = "from features where device_id = :deviceId";
        var queryStatement = this.hibernateReactiveSession.createQuery(query, Feature.class)
                .setParameter("deviceId", deviceId);
        return queryStatement.getResults();
    }

    public Uni<Feature> getFeatureByDeviceAndName(UUID deviceId, String featureName) {
        return this.hibernateReactiveSession.createQuery(
                "from features where device_id = :deviceId and name = :name",
                Feature.class
        )
                .setParameter("deviceId", deviceId)
                .setParameter("name", featureName)
                .getSingleResultOrNull();
    }

    public Uni<Boolean> existsByDeviceAndFeatureId(UUID featureId) {
        return this.hibernateReactiveSession.createNativeQuery("""
                        select count(features.id) > 0 as exists
                        from features
                        where feature_id = :featureId
                        """,
                Boolean.class
        )
                .setParameter("featureId", featureId)
                .getSingleResult();
    }

    public Uni<Feature> storeFeature(Feature feature) {
        return this.hibernateReactiveSession.persist(feature)
                .chain(() -> this.getFeatureById(feature.getFeatureId()));
    }

    public Uni<Feature> getFeatureById(UUID featureId) {
        return this.hibernateReactiveSession.createQuery(
                "from features where feature_id = :featureId",
                Feature.class
        )
                .setParameter("featureId", featureId)
                .getSingleResult();
    }
}
