package red.tetracube.smarthub.properties;

import org.eclipse.microprofile.config.inject.ConfigProperty

interface BrokerConfig {

    @ConfigProperty(name = "client-id")
    fun clientId(): String

    @ConfigProperty(name = "host")
    fun host(): String

    @ConfigProperty(name = "port")
    fun port(): Int

    @ConfigProperty(name = "username")
    fun username(): String

    @ConfigProperty(name = "password")
    fun password(): String

}
