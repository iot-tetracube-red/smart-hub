package red.tetracube.smarthub.iot

import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import com.hivemq.client.mqtt.mqtt5.message.auth.Mqtt5SimpleAuth
import io.quarkus.runtime.StartupEvent
import org.slf4j.LoggerFactory
import red.tetracube.smarthub.configurations.properties.SmartHubConfig
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
class SmartBrokerHub(
    private val smartHubConfig: SmartHubConfig
) {

    private val logger = LoggerFactory.getLogger(SmartBrokerHub::class.java)

    private val mqttClient: Mqtt5AsyncClient

    init {
        this.logger.info("Building MQTT client")
        this.mqttClient = Mqtt5Client.builder()
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

        this.logger.info("Connecting to MQTT broker")
        this.mqttClient.connectWith()
            .cleanStart(true)
            .send()
            .whenComplete { connectionAck, exception ->
                if (exception != null) {
                    this.logger.warn("Cannot connect to MQTT broker, the cloud will not receive or send messages with devices")
                } else {
                    this.logger.info("Connected with MQTT broker ${connectionAck.reasonCode}")
                    this.subscribeTopics()
                }
            }
    }

    fun onStartup(@Observes startupEvent: StartupEvent) {
        this.logger.info("Broker Hub started")
    }

    fun subscribeTopics() {
        this.logger.info("Subscribing to device to the topic ${this.smartHubConfig.iot().topics().deviceProvisioning()}")
        this.mqttClient.subscribeWith()
            .topicFilter(this.smartHubConfig.iot().topics().deviceProvisioning())
            .qos(MqttQos.EXACTLY_ONCE)
            .callback{publish ->
                this.logger.info("Received message ${String(publish.payloadAsBytes)} from the topic ${publish.topic}")
            }
            .send()
    }
}