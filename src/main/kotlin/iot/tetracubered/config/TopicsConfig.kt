package iot.tetracubered.config

import org.eclipse.microprofile.config.inject.ConfigProperty

interface TopicsConfig {

    @ConfigProperty(name = "device-provisioning")
    fun deviceProvisioning(): String
}