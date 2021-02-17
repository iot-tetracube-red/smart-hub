package iot.tetracubered.data.repositories;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.hibernate.reactive.mutiny.Mutiny.Session;

import io.smallrye.mutiny.Multi;
import iot.tetracubered.data.entities.Action;

@ApplicationScoped
public class ActionRepository {

    @Inject
    Session hibernateReactiveSession;

    public Multi<Action> getDeviceActions(UUID featureId) {
        var query = """
                select *
                from actions
                where feature_id = :featureId
                """;
        var preparedQuery = hibernateReactiveSession.createNativeQuery(query, Action.class);
        preparedQuery.setParameter("featureId", featureId);
        return preparedQuery.getResults();
    }
}
