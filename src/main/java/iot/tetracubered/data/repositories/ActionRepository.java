package iot.tetracubered.data.repositories;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import iot.tetracubered.data.entities.Action;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ActionRepository implements PanacheRepository<Action> {

    public Uni<List<Action>> getDeviceActions(UUID featureId) {
        return this.list("feature_id", featureId);
    }

    public Uni<Boolean> actionExistsByFeatureAndActionId(UUID featureId, UUID actionId) {
        return this.count("where f.feature_id = ?1 and actions.action_id = ?2", featureId, actionId)
                .map(foundActionsCount -> foundActionsCount == 1);
    }

    public Uni<Action> getActionByFeatureAndActionId(UUID featureId, UUID actionId) {
        return this.find("where f.feature_id = ?1 and actions.action_id = ?2", featureId, actionId)
                .firstResult();
    }

    public Uni<Action> saveAction(Action action) {
        return this.saveAction(action);
    }

    public Uni<String> getActionTriggerTopicByName(String deviceName, String featureName, String actionName) {
        return this.find("where d.name = ?1 and f.name = ?2 and a.name = ?3", deviceName, featureName, actionName)
                .firstResult()
                .map(Action::getName);
    }
}
