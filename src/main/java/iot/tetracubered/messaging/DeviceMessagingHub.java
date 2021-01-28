package iot.tetracubered.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivemq.client.internal.mqtt.message.publish.MqttPublish;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.Mqtt5RxClient;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.eventbus.EventBus;
import iot.tetracubered.configurations.SmartHubConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.JsonObject;
import java.io.IOException;

@ApplicationScoped
public class DeviceMessagingHub {

    @Inject
    Vertx vertx;

    @Inject
    SmartHubConfiguration smartHubConfiguration;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    EventBus eventBus;

    private Mqtt5AsyncClient mqttClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceMessagingHub.class);

    public DeviceMessagingHub() {
    }

    public void startUp(@Observes StartupEvent startupEvent) {
        LOGGER.info("Building MQTT client");
        this.mqttClient = MqttClient.builder()
                .identifier(this.smartHubConfiguration.messagingClient().clientId())
                .serverHost(this.smartHubConfiguration.messagingClient().host())
                .useMqttVersion5()
                .buildAsync();

        this.subscribeDeviceProvisioning();

        final var username = this.smartHubConfiguration.messagingClient().userName();
        final var password = this.smartHubConfiguration.messagingClient().password().getBytes();
        this.mqttClient.connectWith()
                .cleanStart(true)
                .keepAlive(60)
                .simpleAuth()
                .username(username)
                .password(password)
                .applySimpleAuth()
                .send()
                .whenComplete((mqtt5ConnAck, throwable) -> {
                    if (throwable != null) {
                        LOGGER.error("Cannot connect to MQTT broker");
                        return;
                    }

                    LOGGER.info("Connected to MQTT");
                });
    }

    private void subscribeDeviceProvisioning() {
        this.mqttClient.subscribeWith()
                .topicFilter("device/provisioning")
                .qos(MqttQos.AT_LEAST_ONCE)
                .callback(mqtt5Publish -> {
                    LOGGER.info("Arrived message in topic device/provisioning " + new String(mqtt5Publish.getPayloadAsBytes()));
                    this.eventBus.sendAndForget("device-provisioning", mqtt5Publish.getPayloadAsBytes());
                })
                .send();
    }
}
