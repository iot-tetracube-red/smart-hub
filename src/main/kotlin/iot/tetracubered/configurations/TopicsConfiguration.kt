package iot.tetracubered.configurations

import org.eclipse.microprofile.config.inject.ConfigProperty

interface TopicsConfiguration {

    @ConfigProperty(name = "device-provisioning")
    fun deviceProvisioning(): String
}