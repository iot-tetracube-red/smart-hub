package iot.tetracubered.mqttClient;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.vertx.ConsumeEvent;
import iot.tetracubered.configurations.SmartHubConfiguration;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
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

    @Inject
    MqttClientCallbacks mqttClientCallbacks;

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
        this.mqttClient.setCallback(this.mqttClientCallbacks);
        LOGGER.info("Setting MQTT connection options");
        var connectionOptions = new MqttConnectionOptions();
        connectionOptions.setCleanStart(true);
        LOGGER.info("Connecting to MQTT broker");
        this.mqttClient.connect(connectionOptions);
        LOGGER.info("Subscribing to topics");
        this.mqttClient.subscribe(this.smartHubConfiguration.mqttClient().topics().deviceProvisioning(), 2);
    }

    @ConsumeEvent("query-action")
    public Boolean queryAction(String topic) {
        try {
            this.mqttClient.publish(topic, "1".getBytes(), 2, false);
            return true;
        } catch (MqttException e) {
            return false;
        }
    }
}
