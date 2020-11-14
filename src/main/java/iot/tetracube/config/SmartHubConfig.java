package iot.tetracube.config;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "smart-hub")
public interface SmartHubConfig {

    @ConfigProperty(name = "mqtt-broker")
    MqttBroker mqttBroker();

}
