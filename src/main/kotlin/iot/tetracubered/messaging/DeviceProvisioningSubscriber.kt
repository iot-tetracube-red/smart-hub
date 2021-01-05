package iot.tetracubered.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.mqtt.annotation.MqttSubscriber
import io.micronaut.mqtt.annotation.Topic
import iot.tetracubered.messaging.payloads.DeviceProvisioningPayload
import org.slf4j.LoggerFactory

@MqttSubscriber
class DeviceProvisioningSubscriber {

    private val logger = LoggerFactory.getLogger(DeviceProvisioningSubscriber::class.java)
    private val objectMapper: ObjectMapper = ObjectMapper()

    @Topic("devices/provisioning")
    fun receive(data: ByteArray) {
        logger.info("Received message from devices/provisioning topic: ${String(data)}")
        logger.info("Try to decode")
        val deviceProvisioningMessage = try {
            this.objectMapper.readValue(data, DeviceProvisioningPayload::class.java)
        } catch (ex: Exception) {
            logger.error("Something went wrong by deserializing message... aborting")
            null
        } ?: return
        logger.error("Proceeding with registration of the device identified by circuit ${deviceProvisioningMessage.circuitId}")
    }
}