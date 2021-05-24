package red.tetracube.smarthub.services;

import io.smallrye.mutiny.Uni;
import org.apache.commons.codec.digest.DigestUtils;
import red.tetracube.smarthub.data.entities.BrokerUser;
import red.tetracube.smarthub.data.repositories.BrokerUserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@ApplicationScoped
public class BrokerUserManager {

    @Inject
    BrokerUserRepository brokerUserRepository;

    public Uni<Void> storeUser(String clientId,
                               String username,
                               String rawPassword) {
        var encodedPassword = DigestUtils.sha512Hex(rawPassword);
        return brokerUserRepository.userExists(clientId, username)
                .flatMap(
                        brokerUserExists -> {
                            if (brokerUserExists) {
                                return Uni.createFrom().voidItem();
                            }
                            var brokerUser = new BrokerUser(
                                    0L,
                                    false,
                                    clientId,
                                    username,
                                    encodedPassword
                            );
                            return brokerUserRepository.createUser(brokerUser);
                        });
    }
}