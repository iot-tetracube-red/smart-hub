package iot.tetracubered.mqttClient;

import io.quarkus.runtime.StartupEvent;
import iot.tetracubered.configurations.SmartHubConfiguration;
import org.eclipse.paho.mqttv5.client.*;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class MqttClientHub {

    @Inject
    SmartHubConfiguration smartHubConfiguration;

    private MqttClient mqttClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttClientHub.class);

    public void startUp(@Observes StartupEvent startupEvent) throws MqttException {
        LOGGER.info("Building MQTT client");
        var uri = "tcp://"
                + this.smartHubConfiguration.mqttClient().host()
                + ":"
                + this.smartHubConfiguration.mqttClient().port();
        this.mqttClient = new MqttClient(
                uri,
                this.smartHubConfiguration.mqttClient().clientId(),
                new MemoryPersistence()
        );
        LOGGER.info("Setting MQTT callback class");
        this.mqttClient.setCallback(new MqttClientCallbacks());
        LOGGER.info("Setting MQTT connection options");
        var connectionOptions = new MqttConnectionOptions();
        connectionOptions.setCleanStart(true);
        LOGGER.info("Connecting to MQTT broker");
        this.mqttClient.connect(connectionOptions);
        LOGGER.info("Subscribing to topics");
        this.mqttClient.subscribe(this.smartHubConfiguration.mqttClient().topics().deviceProvisioning(), 2);
    }
}
