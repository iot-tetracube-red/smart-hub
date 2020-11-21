package iot.tetracube.devices;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "smart-hub")
public interface SmartHubConfig {

    @ConfigProperty(name = "mqtt-broker")
    MqttBroker mqttBroker();

    @ConfigProperty(name = "queues")
    RabbitMQQueues queues();

}
