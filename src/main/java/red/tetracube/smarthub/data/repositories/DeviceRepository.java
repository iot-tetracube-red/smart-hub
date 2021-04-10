package red.tetracube.smarthub.data.repositories;

import org.hibernate.reactive.mutiny.Mutiny;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DeviceRepository {

    private final Mutiny.Session reactiveSession;

    public DeviceRepository(Mutiny.Session reactiveSession) {
        this.reactiveSession = reactiveSession;
    }
}
