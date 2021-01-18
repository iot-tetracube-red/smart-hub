package iot.tetracubered.data.repositories;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import iot.tetracubered.data.entities.Device;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class DeviceRepository {

    @Inject
    PgPool pgPool;

    public Multi<Device> getDevices() {
        final var query = """
                select *
                from devices
                 """;
        return this.pgPool.query(query).execute()
                .onItem()
                .transformToMulti(rowRowIterator -> Multi.createFrom().iterable(rowRowIterator))
                .onItem()
                .transform(Device::new);
    }
}
