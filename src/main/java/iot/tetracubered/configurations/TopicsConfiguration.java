package iot.tetracubered.configurations;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public interface TopicsConfiguration {

    @ConfigProperty(name = "device-provisioning")
    String deviceProvisioning();
}
