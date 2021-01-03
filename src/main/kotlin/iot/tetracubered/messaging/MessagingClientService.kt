package iot.tetracubered.messaging

import io.quarkus.runtime.StartupEvent
import io.vertx.core.logging.LoggerFactory
import io.vertx.mqtt.MqttClientOptions
import io.vertx.mutiny.core.Vertx
import io.vertx.mutiny.mqtt.MqttClient
import iot.tetracubered.config.SmartHubConfig
import javax.enterprise.event.Observes
import javax.inject.Singleton

@Singleton
class MessagingClientService(
    private val smartHubConfig: SmartHubConfig,
    private val vertx: Vertx
) {

    private val logger = LoggerFactory.getLogger(MessagingClientService::class.java)

    private val mqttClient: MqttClient

    init {
        val mqttClientOptions = MqttClientOptions()
        mqttClientOptions.password = this.smartHubConfig.messaging().password()
        mqttClientOptions.username = this.smartHubConfig.messaging().user()
        this.mqttClient = MqttClient.create(this.vertx, mqttClientOptions)
    }

    fun startup(@Observes startupEvent: StartupEvent) {
        this.mqttClient.connect(this.smartHubConfig.messaging().port(), this.smartHubConfig.messaging().host())
            .onItem()
            .invoke { _ ->
                logger.info("MQTT client connection success")
                logger.info("Subscribing to device provisioning topic")
                this.subscribeTopics()
            }
            .subscribe()
            .with {
                logger.info("Subscribed")
            }
    }

    private fun subscribeTopics() {
        val topics = HashMap<String, Int>()
        topics[this.smartHubConfig.messaging().topics().deviceProvisioning()] = 2
        this.mqttClient.subscribe(topics)
            .onItem()
            .invoke { _ ->
                logger.info("Subscribed to MQTT topics")
            }
            .subscribe()
            .with {
                logger.info("Subscribed")
            }
        this.mqttClient.publishHandler { message ->
            when (message.topicName()) {
                this.smartHubConfig.messaging().topics().deviceProvisioning() -> handleDeviceProvisioningMessage(message.payload().bytes)
            }
        }
    }

    private fun handleDeviceProvisioningMessage(message: ByteArray) {
        logger.info("Arrived device provisioning message: ${String(message)}")
    }
}