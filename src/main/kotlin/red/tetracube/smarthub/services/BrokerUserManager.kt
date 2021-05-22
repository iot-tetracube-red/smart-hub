package red.tetracube.smarthub.services

import io.smallrye.mutiny.Uni
import org.apache.commons.codec.digest.DigestUtils
import red.tetracube.smarthub.data.entities.BrokerUser
import red.tetracube.smarthub.data.repositories.BrokerUserRepository
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class BrokerUserManager(
    private val brokerUserRepository: BrokerUserRepository
) {

    fun storeUser(
        clientId: String,
        username: String,
        rawPassword: String
    ): Uni<Void> {
        val encodedPassword = DigestUtils.sha512Hex(rawPassword)
        return brokerUserRepository.userExists(clientId, username)
            .flatMap { brokerUserExists ->
                if (brokerUserExists) {
                    Uni.createFrom().voidItem()
                } else {
                    val brokerUser = BrokerUser(
                        0L,
                        false,
                        clientId,
                        username,
                        encodedPassword,
                        null
                    )
                    brokerUserRepository.createUser(brokerUser)
                }
            }
    }
}