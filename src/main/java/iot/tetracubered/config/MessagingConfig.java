package iot.tetracubered.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public interface MessagingConfig {

    @ConfigProperty(name = "host")
    String host();

    @ConfigProperty(name = "port")
    Integer port();

    @ConfigProperty(name = "user")
    String user();

    @ConfigProperty(name = "password")
    String password();

    @ConfigProperty(name = "topics")
    TopicsConfig topics();
}