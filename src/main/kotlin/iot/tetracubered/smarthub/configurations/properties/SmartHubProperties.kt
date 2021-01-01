package iot.tetracubered.smarthub.configurations.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "smart-hub")
data class SmartHubProperties(
    val deviceProvisioningQueueName: String,
    val deviceProvisioningTopicName: String
)