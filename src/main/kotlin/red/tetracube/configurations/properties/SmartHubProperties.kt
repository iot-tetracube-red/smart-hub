package red.tetracube.configurations.properties

import io.quarkus.arc.config.ConfigProperties
import org.eclipse.microprofile.config.inject.ConfigProperty

@ConfigProperties(prefix = "smart-hub")
interface SmartHubProperties {

    @ConfigProperty(name = "iot")
    fun iot(): IoTProperties
}