package iot.tetracubered.configurations;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public interface MessagingClientConfiguration {

    @ConfigProperty(name = "host")
    String host();

    @ConfigProperty(name = "port")
    Integer port();

    @ConfigProperty(name = "client-id")
    String clientId();

    @ConfigProperty(name = "user-name")
    String userName();

    @ConfigProperty(name = "password")
    String password();

    @ConfigProperty(name = "topics")
    TopicsConfiguration topics();
}
