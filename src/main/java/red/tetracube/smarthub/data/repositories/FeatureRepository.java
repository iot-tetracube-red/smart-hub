package red.tetracube.smarthub.data.repositories;

import org.hibernate.reactive.mutiny.Mutiny;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FeatureRepository {

    private final Mutiny.Session reactiveSession;

    public FeatureRepository(Mutiny.Session reactiveSession) {
        this.reactiveSession = reactiveSession;
    }
}
