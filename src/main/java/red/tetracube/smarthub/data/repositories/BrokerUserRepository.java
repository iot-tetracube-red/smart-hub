package red.tetracube.smarthub.data.repositories;

import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import red.tetracube.smarthub.data.entities.BrokerUser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class BrokerUserRepository {

    @Inject
    Mutiny.SessionFactory rxSessionFactory;

    public Uni<Void> createUser(BrokerUser brokerUser) {
        var rxSession = rxSessionFactory.openSession();
        return rxSession.persist(brokerUser)
                .call(ignored -> rxSession.flush());
    }

    public Uni<Boolean> userExists(String clientId,
                                   String username) {
        var rxSession = rxSessionFactory.openSession();
        return rxSession.createNativeQuery("""
                        select (count(id) > 0) as exists
                        from vmq_auth_acl
                        where username = :username and client_id = :clientId
                         """,
                Boolean.class
        )
                .setParameter("username", username)
                .setParameter("clientId", clientId)
                .getSingleResultOrNull()
                .call(ignored -> rxSession.flush());
    }
}
