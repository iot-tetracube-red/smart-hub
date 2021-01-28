package iot.tetracubered.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.eventbus.EventBus;
import iot.tetracubered.configurations.SmartHubConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

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

    private MqttClient mqttClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceMessagingHub.class);

    public DeviceMessagingHub() {
        var mqttClientOptions = new MqttClientOptions();
        mqttClientOptions = this.smartHubConfiguration.messagingClient().userName();
        mqttClientOptions.password = this.smartHubConfiguration.messagingClient().password();
        this.mqttClient = MqttClient.create(this.vertx, mqttClientOptions);
    }

    public void startUp(@Observes StartupEvent startupEvent) {
        this.mqtt
    }
}
