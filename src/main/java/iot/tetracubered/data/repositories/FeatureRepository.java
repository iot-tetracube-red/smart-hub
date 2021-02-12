package iot.tetracubered.data.repositories;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import iot.tetracubered.data.entities.Feature;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FeatureRepository implements PanacheRepository<Feature> {

    public Multi<Feature> getAllFeatures() {
        return this.listAll()
                .onItem()
                .transformToMulti(features -> Multi.createFrom().items(features.stream()));
    }

    public Uni<Feature> getFeatureByDeviceAndFeatureName(UUID deviceId,
                                                         String featureName) {
        return this.find("where d.id = $1 and f.name = $2", deviceId, featureName)
                .firstResult();
    }

    public Uni<List<Feature>> getDeviceFeatures(UUID deviceId) {
        return this.find("device_id", deviceId)
                .list();
    }

    public Uni<Boolean> existsByDeviceAndFeatureId(UUID circuitId, UUID featureId) {
        return this.count("where d.circuit_id = ?1 and features.feature_id = ?2", circuitId, featureId)
                .map(foundFeatures -> foundFeatures == 1);
    }

    public Uni<Feature> getFeatureByDeviceAndFeatureId(UUID circuitId, UUID featureId) {
        return this.find("where d.circuit_id = ?1 and features.feature_id = ?2", circuitId, featureId)
                .firstResult();
    }

    public Uni<Feature> saveFeature(Feature feature) {
        return this.saveFeature(feature);
    }
}
