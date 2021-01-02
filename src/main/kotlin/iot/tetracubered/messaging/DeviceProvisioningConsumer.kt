package iot.tetracubered.messaging

import io.quarkus.vertx.ConsumeEvent
import io.vertx.mutiny.mqtt.MqttClient
import iot.tetracubered.configurations.SmartHubConfiguration
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class DeviceProvisioningConsumer(
    private val smartHubConfiguration: SmartHubConfiguration
) {

    private val logger = LoggerFactory.getLogger(DeviceProvisioningConsumer::class.java)

    @ConsumeEvent(value = "messaging-client-started")
    fun manageDeviceProvisioningTopic(mqttClient: MqttClient) {
        mqttClient.subscribe(this.smartHubConfiguration.mqtt().topics().deviceProvisioning(), 2)
            .subscribe()
            .with {
                logger.info("Subscribed device provisioning")
                mqttClient.publishHandler { message ->
                    if (message.topicName() == this.smartHubConfiguration.mqtt().topics().deviceProvisioning()) {
                        this.consumeDeviceProvisioning(message.payload().bytes)
                    }
                }
            }
    }

    private fun consumeDeviceProvisioning(message: ByteArray) {
        logger.info("Arrived message in device-provisioning topic ${String(message)}")
    }
}