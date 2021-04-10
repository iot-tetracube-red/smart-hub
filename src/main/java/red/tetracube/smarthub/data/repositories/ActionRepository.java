package red.tetracube.smarthub.data.repositories;

import org.hibernate.reactive.mutiny.Mutiny;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ActionRepository {

    private final Mutiny.Session reactiveSession;

    public ActionRepository(Mutiny.Session reactiveSession) {
        this.reactiveSession = reactiveSession;
    }
}
