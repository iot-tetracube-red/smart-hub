package red.tetracube.smarthub.services;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import red.tetracube.smarthub.data.entities.BrokerUser;
import red.tetracube.smarthub.data.repositories.BrokerUserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class BrokerUserManager {

    @Inject
    BrokerUserRepository brokerUserRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public BrokerUserManager() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public Uni<Void> storeUser(String clientId,
                               String username,
                               String rawPassword,
                               List<String> publishTopics,
                               List<String> subscribeTopics) {
        return brokerUserRepository.userExists(clientId, username)
                .flatMap(brokerUserExists -> {
                    if (brokerUserExists) {
                        return Uni.createFrom().voidItem();
                    }

                    var brokerUser = new BrokerUser(
                            UUID.randomUUID(),
                            "",
                            clientId,
                            username,
                            passwordEncoder.encode(rawPassword),
                            createAcl(publishTopics),
                            createAcl(subscribeTopics)
                    );
                    return brokerUserRepository.createUser(brokerUser);
                });
    }

    private JsonArray createAcl(List<String> acl) {
        var patterns = acl.stream()
                .map(item ->
                        new JsonObject(
                                Collections.singletonMap("pattern", item)
                        )
                )
                .collect(Collectors.toList());
        return new JsonArray(patterns);
    }
}
