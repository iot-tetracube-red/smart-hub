package iot.tetracubered.messaging;

import io.quarkus.runtime.StartupEvent;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.mqtt.MqttClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.mqtt.MqttClient;
import iot.tetracubered.config.SmartHubConfig;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;
import java.util.HashMap;

@Singleton
public class MessagingClientService {

    private final static Logger LOGGER = LoggerFactory.getLogger(MessagingClientService.class);

    private final MqttClient mqttClient;
    private final SmartHubConfig smartHubConfig;

    public MessagingClientService(SmartHubConfig smartHubConfig,
                                  Vertx vertx) {
        this.smartHubConfig = smartHubConfig;

        var mqttClientOptions = new MqttClientOptions();
        mqttClientOptions.setPassword(this.smartHubConfig.messaging().password());
        mqttClientOptions.setUsername(this.smartHubConfig.messaging().user());
        this.mqttClient = MqttClient.create(vertx, mqttClientOptions);
    }

    public void startup(@Observes StartupEvent startupEven) {
        this.mqttClient.connect(this.smartHubConfig.messaging().port(), this.smartHubConfig.messaging().host())
                .onItem()
                .invoke(event -> {
                    LOGGER.info("MQTT client connection success");
                    LOGGER.info("Subscribing to device provisioning topic");
                    this.subscribeTopics();
                })
                .subscribe()
                .with(subscription -> LOGGER.info("Subscribed"));
    }

    private void subscribeTopics() {
        var topics = new HashMap<String, Integer>();
        topics.put(this.smartHubConfig.messaging().topics().deviceProvisioning(), 2);
        this.mqttClient.subscribe(topics)
                .onItem()
                .invoke(subscribeEvent -> LOGGER.info("Subscribed to MQTT topics"))
                .subscribe()
                .with(event -> LOGGER.info("Subscribed"));

        this.mqttClient.publishHandler(message -> {
            if (message.topicName().equals(this.smartHubConfig.messaging().topics().deviceProvisioning())) {
                handleDeviceProvisioningMessage(message.payload().getBytes());
            }
        });
    }

    private void handleDeviceProvisioningMessage(byte[] message) {
        LOGGER.info("Arrived device provisioning message: " + new String(message));
    }
}