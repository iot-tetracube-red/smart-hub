package red.tetracube.smarthub.properties;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public interface IoTConfig {

    @ConfigProperty(name = "broker")
    BrokerConfig broker();

    @ConfigProperty(name = "topics")
    TopicsConfig topics();
}