package iot.tetracubered.configurations

import io.quarkus.arc.config.ConfigProperties
import org.eclipse.microprofile.config.inject.ConfigProperty

@ConfigProperties(prefix = "smart-hub")
interface SmartHubConfiguration {

    @ConfigProperty(name = "mqtt")
    fun mqtt(): MqttConfiguration

}
