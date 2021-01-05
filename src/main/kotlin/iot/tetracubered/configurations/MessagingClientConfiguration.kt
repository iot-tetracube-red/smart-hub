package iot.tetracubered.configurations

import org.eclipse.microprofile.config.inject.ConfigProperty

interface MessagingClientConfiguration {

    @ConfigProperty(name = "host")
    fun host(): String

    @ConfigProperty(name = "port")
    fun port(): Int

    @ConfigProperty(name = "client-id")
    fun clientId(): String

    @ConfigProperty(name = "user-name")
    fun userName(): String

    @ConfigProperty(name = "password")
    fun password(): String

    @ConfigProperty(name = "topics")
    fun topics(): TopicsConfiguration
}