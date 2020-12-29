package iot.tetracubered.data.repositories;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class DeviceRepository {

    @Inject
    PgPool pgPool;

    private Uni<Boolean> existsByCircuitId(UUID circuitId) {
        pgPool.
        return Uni.createFrom().item(false);
    }
}
