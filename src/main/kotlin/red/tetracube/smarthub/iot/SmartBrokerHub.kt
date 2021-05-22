package red.tetracube.smarthub.iot

import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import com.hivemq.client.mqtt.mqtt5.message.auth.Mqtt5SimpleAuth
import org.slf4j.LoggerFactory
import red.tetracube.smarthub.properties.SmartHubConfig
import red.tetracube.smarthub.services.BrokerUserManager
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class SmartBrokerHub(
    private val brokerUserManager: BrokerUserManager,
    private val smartHubConfig: SmartHubConfig
) {

    private lateinit var mqttClient: Mqtt5AsyncClient
    private val logger = LoggerFactory.getLogger(SmartBrokerHub::class.java)

    fun setupMqttConnection() {
        brokerUserManager.storeUser(
            this.smartHubConfig.iot().broker().clientId(),
            this.smartHubConfig.iot().broker().username(),
            this.smartHubConfig.iot().broker().password()
        )
            .subscribe()
            .with { _ ->
                logger.info("Building MQTT client")
                mqttClient = Mqtt5Client.builder()
                    .identifier(this.smartHubConfig.iot().broker().clientId())
                    .serverHost(this.smartHubConfig.iot().broker().host())
                    .serverPort(this.smartHubConfig.iot().broker().port())
                    .simpleAuth(
                        Mqtt5SimpleAuth.builder()
                            .username(this.smartHubConfig.iot().broker().username())
                            .password(this.smartHubConfig.iot().broker().password().encodeToByteArray())
                            .build()
                    )
                    .buildAsync()

                logger.info("Connecting to MQTT broker")
                mqttClient.connectWith()
                    .cleanStart(true)
                    .send()
                    .whenComplete { connectionAck, exception ->
                        if (exception != null) {
                            logger.warn("Cannot connect to MQTT broker, the cloud will not receive or send messages with devices")
                        } else {
                            logger.info("Connected with MQTT broker ${connectionAck.reasonCode}")
                            subscribeTopics()
                        }
                    }
            }
    }

    private fun subscribeTopics() {
        logger.info("Subscribing to device to the topic {}", smartHubConfig.iot().topics().deviceProvisioning())
        mqttClient.subscribeWith()
            .topicFilter(smartHubConfig.iot().topics().deviceProvisioning())
            .qos(MqttQos.EXACTLY_ONCE)
            .callback { publish ->
                logger.debug("Received message ${String(publish.payloadAsBytes)} from the topic ${publish.topic}")
                // eventBus.sendAndForget("device-provisioning", publish.getPayloadAsBytes())
            }
            .send()
    }
}
