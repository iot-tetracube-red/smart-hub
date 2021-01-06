package iot.tetracubered.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.runtime.StartupEvent
import io.quarkus.vertx.ConsumeEvent
import io.vertx.mqtt.MqttClientOptions
import io.vertx.mutiny.core.Vertx
import io.vertx.mutiny.core.eventbus.EventBus
import io.vertx.mutiny.mqtt.MqttClient
import iot.tetracubered.configurations.SmartHubConfiguration
import iot.tetracubered.messaging.payloads.DeviceFeedbackMessage
import iot.tetracubered.messaging.payloads.DeviceProvisioningPayload
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
class DeviceMessagingHub(
    private val vertx: Vertx,
    private val smartHubConfiguration: SmartHubConfiguration,
    private val objectMapper: ObjectMapper,
    private val eventBus: EventBus
) {

    private val logger = LoggerFactory.getLogger(DeviceMessagingHub::class.java)

    private val mqttClient: MqttClient

    init {
        val mqttClientOptions = MqttClientOptions()
        mqttClientOptions.username = this.smartHubConfiguration.messagingClient().userName()
        mqttClientOptions.password = this.smartHubConfiguration.messagingClient().password()
        this.mqttClient = MqttClient.create(this.vertx, mqttClientOptions)
    }

    fun startup(@Observes startupEvent: StartupEvent) {
        this.mqttClient.connect(
            this.smartHubConfiguration.messagingClient().port(),
            this.smartHubConfiguration.messagingClient().host()
        )
            .invoke { _ -> this.subscribeTopics() }
            .subscribe()
            .with { logger.info("MQTT connection success") }
    }

    @ConsumeEvent("send-feedback")
    fun sendDeviceFeedback(deviceFeedbackMessage: DeviceFeedbackMessage) {

    }

    private fun subscribeTopics() {
        val topics = HashMap<String, Int>()
        topics[this.smartHubConfiguration.messagingClient().topics().deviceProvisioning()] = 2
        this.mqttClient.subscribe(topics)
            .subscribe()
            .with { logger.info("Subscriptions topics successful") }
        this.mqttClient.publishHandler { mqttPublishMessage ->
            logger.info("Message arrived in topic ${mqttPublishMessage.topicName()} containing ${String(mqttPublishMessage.payload().bytes)}")
            if (mqttPublishMessage.topicName() == this.smartHubConfiguration.messagingClient().topics().deviceProvisioning()) {
                this.handleDeviceProvisioningMessage(mqttPublishMessage.payload().bytes)
            }
        }
    }

    private fun handleDeviceProvisioningMessage(message: ByteArray) {
        logger.info("Try to decode")
        val deviceProvisioningMessage = try {
            this.objectMapper.readValue(message, DeviceProvisioningPayload::class.java)
        } catch (ex: Exception) {
            logger.error("Something went wrong by deserializing message... aborting")
            null
        } ?: return
        logger.info("Proceeding with registration of the device identified by circuit ${deviceProvisioningMessage.circuitId}")
        this.eventBus.publish("device-provisioning", deviceProvisioningMessage)
    }
}