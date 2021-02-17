package iot.tetracubered.properties;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "smart-hub")
public interface SmartHubProperties {

    @ConfigProperty(name = "topics")
    TopicsProperties topics();
}
