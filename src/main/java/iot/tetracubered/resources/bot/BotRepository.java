package iot.tetracubered.resources.bot;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class BotRepository {

    @Inject
    PgPool pgPool;

    Multi<Row> getDevicesAndFeaturesNames() {
        var query = """
                select d.name as device_name and f.name as feature_name
                from devices d 
                inner join features f on f.device_id = d.id
                """;
        return this.pgPool.preparedQuery(query).execute()
                .onItem()
                .transformToMulti(rowRowIterator -> Multi.createFrom().iterable(rowRowIterator));
    }
}
