package red.tetracube.smarthub.services;

import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import red.tetracube.smarthub.data.entities.Telemetry;
import red.tetracube.smarthub.data.repositories.TelemetryRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class TelemetryService {

    @Inject
    TelemetryRepository telemetryRepository;

    private final static Logger LOGGER = LoggerFactory.getLogger(TelemetryService.class);

    public Uni<Void> storeTelemetryData(UUID featureId, float value, LocalDateTime probedAt) {
        LOGGER.info("Storing value {} for feature id {}", value, featureId);
        return telemetryRepository.storeTelemetryData(
                new Telemetry(
                        UUID.randomUUID(),
                        value,
                        LocalDateTime.now(),
                        probedAt,
                        featureId
                )
        );
    }
}
