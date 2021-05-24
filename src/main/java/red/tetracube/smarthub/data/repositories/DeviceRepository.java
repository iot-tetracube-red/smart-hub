package red.tetracube.smarthub.data.repositories;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import red.tetracube.smarthub.annotations.processors.EntityProcessor;
import red.tetracube.smarthub.data.entities.Device;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class DeviceRepository {

    @Inject
    PgPool pgPool;

    @Inject
    EntityProcessor entityProcessor;

    public Uni<Optional<Device>> insertOrUpdateDevice(Device device) {
        var query = """
                INSERT INTO devices (id, name, is_online, feedback_topic, color_code, created_at, updated_at)
                VALUES ($1, $2, $3, $4, $5, $6, $7)
                ON CONFLICT (id) DO
                UPDATE SET is_online = $3, feedback_topic = $4, updated_at = $7;
                """;
        var parameters = Tuple.of(
                device.getId(),
                device.getName(),
                device.isOnline(),
                device.getFeedbackTopic(),
                device.getColorCode()
        );
        parameters = parameters.addLocalDateTime(device.getCreatedAt());
        parameters = parameters.addLocalDateTime(device.getUpdatedAt());
        return pgPool.preparedQuery(query)
                .execute(parameters)
                .flatMap(ignored -> getDeviceById(device.getId()));
    }

    public Uni<Optional<Device>> getDeviceById(UUID id) {
        return pgPool.preparedQuery("""
                select id, name, is_online, feedback_topic, color_code, created_at, updated_at
                from devices
                where id = $1
                """)
                .execute(Tuple.of(id))
                .map(rows -> {
                    var rowsIterator = rows.iterator();
                    if (!rowsIterator.hasNext()) {
                        return Optional.empty();
                    }
                    var row = rowsIterator.next();
                    try {
                        var deviceEntity = entityProcessor.mapTableToEntity(new Device(), row);
                        return Optional.of(deviceEntity);
                    } catch (IllegalAccessException e) {
                        return Optional.empty();
                    }
                });
    }
}