package red.tetracube.smarthub.data.repositories;

import io.vertx.mutiny.pgclient.PgPool;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TelemetryRepository {

    @Inject
    PgPool pgPool;
}
