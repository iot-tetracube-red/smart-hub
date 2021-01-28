package iot.tetracubered.data.repositories;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import iot.tetracubered.data.entities.Feature;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class FeatureRepository {

    @Inject
    PgPool pgPool;

    public Multi<Feature> getDeviceFeatures(UUID deviceId) {
        final var query = """
                select *
                from features
                where device_id = $1
                """;
        final var queryParameters = Tuple.of(deviceId);
        return this.pgPool.preparedQuery(query).execute(queryParameters)
                .onItem().transformToMulti(rows -> Multi.createFrom().iterable(rows))
                .map(Feature::new)
                .onFailure().invoke(Throwable::printStackTrace);
    }
}
