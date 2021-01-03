package iot.tetracubered.config

import io.quarkus.arc.config.ConfigProperties
import org.eclipse.microprofile.config.inject.ConfigProperty

@ConfigProperties(prefix = "smart-hub")
interface SmartHubConfig {

    @ConfigProperty(name = "messaging")
    fun messaging(): MessagingConfig
}