package iot.tetracubered.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public interface TopicsConfig {

    @ConfigProperty(name = "device-provisioning")
    String deviceProvisioning();
}