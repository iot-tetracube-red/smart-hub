package iot.tetracubered.messaging

import io.quarkus.runtime.StartupEvent
import io.vertx.mqtt.MqttClientOptions
import io.vertx.mutiny.core.Vertx
import io.vertx.mutiny.core.eventbus.EventBus
import io.vertx.mutiny.mqtt.MqttClient
import iot.tetracubered.configurations.SmartHubConfiguration
import org.slf4j.LoggerFactory
import javax.enterprise.event.Observes
import javax.inject.Singleton

@Singleton
class MessagingClient(
    private val vertx: Vertx,
    private val eventBus: EventBus,
    private val smartHubConfiguration: SmartHubConfiguration
) {

    private val logger = LoggerFactory.getLogger(MessagingClient::class.java)

    fun startup(@Observes event: StartupEvent) {
        val connectionOptions = MqttClientOptions()
        connectionOptions.username = this.smartHubConfiguration.mqtt().username()
        connectionOptions.password = this.smartHubConfiguration.mqtt().password()
        val mqttClient = MqttClient.create(this.vertx, connectionOptions)
        mqttClient.connect(this.smartHubConfiguration.mqtt().port(), this.smartHubConfiguration.mqtt().host())
            .subscribe()
            .with {
                logger.info("Mqtt is connected")
                this.eventBus.sendAndForget("messaging-client-started", mqttClient)
            }
    }
}