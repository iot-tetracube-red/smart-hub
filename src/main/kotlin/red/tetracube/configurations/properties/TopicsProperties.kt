package red.tetracube.configurations.properties

import org.eclipse.microprofile.config.inject.ConfigProperty

interface TopicsProperties {

    @ConfigProperty(name = "device-provisioning")
    fun deviceProvisioning(): String
}