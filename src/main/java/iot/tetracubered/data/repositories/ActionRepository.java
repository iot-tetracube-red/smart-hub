package iot.tetracubered.data.repositories;

import io.smallrye.mutiny.Uni;
import iot.tetracubered.data.entities.Action;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class ActionRepository {

    @Inject
    Mutiny.Session sqlRxSession;

    public Uni<Action> storeAction(Action action) {
        return this.sqlRxSession.persist(action)
                .call(() -> this.sqlRxSession.flush())
                .chain(() -> this.findActionByActionId(action.getActionId()));
    }

    public Uni<Action> findActionByActionId(UUID actionId) {
        var query = """
                select * 
                from actions
                where action_id = :actionId
                """;
        return this.sqlRxSession.createNativeQuery(query, Action.class)
                .setParameter("actionId", actionId)
                .getSingleResultOrNull()
                .call(() -> this.sqlRxSession.flush());
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
