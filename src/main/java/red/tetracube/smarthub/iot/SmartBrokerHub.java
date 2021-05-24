package red.tetracube.smarthub.iot;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.auth.Mqtt5SimpleAuth;
import io.vertx.mutiny.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import red.tetracube.smarthub.properties.SmartHubConfig;
import red.tetracube.smarthub.services.BrokerUserManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class SmartBrokerHub {

    @Inject
    BrokerUserManager brokerUserManager;

    @Inject
    SmartHubConfig smartHubConfig;

    @Inject
    EventBus eventBus;

    private Mqtt5AsyncClient mqttClient;

    private final static Logger LOGGER = LoggerFactory.getLogger(SmartBrokerHub.class);

    public void setupMqttConnection() {
        brokerUserManager.storeUser(
                this.smartHubConfig.iot().broker().clientId(),
                this.smartHubConfig.iot().broker().username(),
                this.smartHubConfig.iot().broker().password()
        )
                .subscribe()
                .with(ignored -> {
                    LOGGER.info("Building MQTT client");
                    mqttClient = Mqtt5Client.builder()
                            .identifier(this.smartHubConfig.iot().broker().clientId())
                            .serverHost(this.smartHubConfig.iot().broker().host())
                            .serverPort(this.smartHubConfig.iot().broker().port())
                            .simpleAuth(
                                    Mqtt5SimpleAuth.builder()
                                            .username(this.smartHubConfig.iot().broker().username())
                                            .password(this.smartHubConfig.iot().broker().password().getBytes(StandardCharsets.UTF_8))
                                            .build()
                            )
                            .buildAsync();

                    LOGGER.info("Connecting to MQTT broker");
                    mqttClient.connectWith()
                            .cleanStart(true)
                            .send()
                            .whenComplete((connectionAck, exception) -> {
                                if (exception != null) {
                                    LOGGER.warn("Cannot connect to MQTT broker, the cloud will not receive or send messages with devices");
                                } else {
                                    LOGGER.info("Connected with MQTT broker {}", connectionAck.getReasonCode());
                                    subscribeTopics();
                                }
                            });
                });
    }

    private void subscribeTopics() {
        LOGGER.info("Subscribing to device to the topic {}", smartHubConfig.iot().topics().deviceProvisioning());
        mqttClient.subscribeWith()
                .topicFilter(smartHubConfig.iot().topics().deviceProvisioning())
                .qos(MqttQos.EXACTLY_ONCE)
                .callback(publish -> {
                    LOGGER.debug("Received message {} from the topic {}", new String(publish.getPayloadAsBytes()), publish.getTopic());
                    eventBus.sendAndForget("device-provisioning", publish.getPayloadAsBytes());
                })
                .send();
    }
}
