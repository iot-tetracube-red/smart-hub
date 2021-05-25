package red.tetracube.smarthub.data.repositories;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import red.tetracube.smarthub.data.entities.Telemetry;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TelemetryRepository {

    @Inject
    PgPool pgPool;

    public Uni<Void> storeTelemetryData(Telemetry telemetry) {
        return pgPool.preparedQuery("""
                insert into telemetry_data (id, value, stored_at, probed_at, feature_id)
                values ($1, $2, $3, $4, $5)
                """)
                .execute(Tuple.of(
                        telemetry.getId(),
                        telemetry.getValue(),
                        telemetry.getStoredAt(),
                        telemetry.getProbedAt(),
                        telemetry.getFeatureId()
                ))
                .map(ignored -> null);
    }
}
