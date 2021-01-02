package iot.tetracubered.configurations

import org.eclipse.microprofile.config.inject.ConfigProperty

interface MqttConfiguration {

    @ConfigProperty(name = "host")
    fun host(): String

    @ConfigProperty(name = "port")
    fun port(): Int

    @ConfigProperty(name = "username")
    fun username(): String

    @ConfigProperty(name = "password")
    fun password(): String

    @ConfigProperty(name = "topics")
    fun topics(): TopicsConfiguration

}
