package red.tetracube.smarthub.configurations.properties;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public interface BrokerConfig {

    @ConfigProperty(name = "client-id")
    String clientId();

    @ConfigProperty(name = "host")
    String host();

    @ConfigProperty(name = "port")
    Integer port();

    @ConfigProperty(name = "username")
    String username();

    @ConfigProperty(name = "password")
    String password();
}
