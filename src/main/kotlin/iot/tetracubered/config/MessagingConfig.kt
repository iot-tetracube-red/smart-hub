package iot.tetracubered.config

import org.eclipse.microprofile.config.inject.ConfigProperty

interface MessagingConfig {

    @ConfigProperty(name = "host")
    fun host(): String

    @ConfigProperty(name = "port")
    fun port(): String

    @ConfigProperty(name = "user")
    fun user(): String

    @ConfigProperty(name = "password")
    fun password(): String

    @ConfigProperty(name = "topics")
    fun topics(): TopicsConfig
}