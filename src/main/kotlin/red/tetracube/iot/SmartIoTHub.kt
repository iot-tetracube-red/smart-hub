package red.tetracube.iot

import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import com.hivemq.client.mqtt.mqtt5.Mqtt5RxClient
import com.hivemq.client.mqtt.mqtt5.message.auth.Mqtt5SimpleAuth
import io.quarkus.runtime.StartupEvent
import io.vertx.core.logging.LoggerFactory
import red.tetracube.configurations.properties.SmartHubProperties
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
class SmartIoTHub(
    private val smartHubProperties: SmartHubProperties
) {

    private val logger = LoggerFactory.getLogger(SmartIoTHub::class.java)
    private val mqttClient: Mqtt5RxClient

    init {
        val simpleAuth = Mqtt5SimpleAuth.builder()
            .username(this.smartHubProperties.iot().username())
            .password(this.smartHubProperties.iot().password().toByteArray())
            .build()

        this.mqttClient = Mqtt5Client.builder()
            .identifier(this.smartHubProperties.iot().clientId())
            .serverHost(this.smartHubProperties.iot().host())
            .serverPort(this.smartHubProperties.iot().port())
            .simpleAuth(simpleAuth)
            .buildRx()

        this.mqttClient.connect()
            .subscribe { _, failure ->
                if (failure != null) {
                    logger.info("Cannot connect to MQTT broker due ${failure.message}")
                } else {
                    logger.info("Smart Hub connected correctly to MQTT broker")
                    this.subscribeTopics()
                }
            }
    }

    fun onStartup(@Observes startupEvent: StartupEvent) {
        logger.info("The IoT Hub has started")
    }

    private fun subscribeTopics() {
        this.mqttClient.subscribePublishesWith()
            .addSubscription()
            .topicFilter(this.smartHubProperties.iot().topics().deviceProvisioning())
            .qos(MqttQos.EXACTLY_ONCE)
            .applySubscription()
            .applySubscribe()
            .doOnSingle {
                this.logger.info("Subscribed to device provisioning topic ${it.reasonCodes}")
            }
            .doOnNext { publish ->
                logger.info("Received message from topic ${publish.topic} with content ${String(publish.payloadAsBytes)}")
            }
            .subscribe()
    }
}