package iot.tetracube.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public interface MqttBroker {

    @ConfigProperty(name = "host")
    String host();

    @ConfigProperty(name = "port")
    Integer port();

    @ConfigProperty(name = "username")
    String username();

    @ConfigProperty(name = "password")
    String password();

    @ConfigProperty(name = "mqtt-exchange")
    String mqttExchange();

}
