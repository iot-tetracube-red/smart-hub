package iot.tetracube.config;

public interface MqttBroker {

    String host();
    Integer port();
    String username();
    String password();

}
