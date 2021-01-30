package iot.tetracubered.configurations;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "smart-hub")
public interface SmartHubConfiguration {

    @ConfigProperty(name = "mqtt-client")
    MqttClientConfiguration mqttClient();
}
