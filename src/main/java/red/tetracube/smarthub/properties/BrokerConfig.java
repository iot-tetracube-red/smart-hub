package red.tetracube.smarthub.properties;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public interface BrokerConfig {

    @ConfigProperty(name = "client-id")
    String clientId();

    @ConfigProperty(name = "host")
    String host();

    @ConfigProperty(name = "port")
    int port();

    @ConfigProperty(name = "username")
    String username();

    @ConfigProperty(name = "password")
    String password();
}
