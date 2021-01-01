package iot.tetracubered.smarthub.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import iot.tetracubered.smarthub.messaging.payloads.DeviceProvisioning
import iot.tetracubered.smarthub.services.DeviceProvisioningService
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import org.springframework.validation.BindException
import org.springframework.validation.Errors
import org.springframework.validation.Validator

@Component
class DeviceMessagesConsumers(
    private val objectMapper: ObjectMapper,
    private val webFluxValidator: Validator,
    private val rabbitTemplate: RabbitTemplate,
    private val deviceProvisioningService: DeviceProvisioningService
) {

    private val logger = LoggerFactory.getLogger(DeviceMessagesConsumers::class.java)

    @RabbitHandler
    @RabbitListener(queues = ["device-provisioning"])
    fun receiveProvisioningMessages(message: ByteArray) {
        this.logger.info("Received message ${String(message)}")
        val deviceProvisioning = try {
            objectMapper.readValue(message, DeviceProvisioning::class.java)
        } catch (exception: Exception) {
            this.logger.info("Cannot parse message body")
            null
        } ?: return
        val errors: Errors = BindException(deviceProvisioning, DeviceProvisioning::class.java.canonicalName)
        this.webFluxValidator.validate(deviceProvisioning, errors)
        if (errors.hasErrors()) {
            this.logger.error("The provisioning message contains errors")
        }
        runBlocking {
            deviceProvisioningService.manageDeviceProvisioning(deviceProvisioning)
        }
        val topic = deviceProvisioning.feedbackTopic.replace("/", ".")
        try {
            this.rabbitTemplate.convertAndSend("amq.topic", topic, "1".encodeToByteArray())
        } catch (ex: Exception) {
            this.logger.error("Cannot deliver feedback message")
        }
    }
}