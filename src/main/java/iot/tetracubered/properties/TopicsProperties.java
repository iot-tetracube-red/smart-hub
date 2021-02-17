package iot.tetracubered.properties;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public interface TopicsProperties {

    @ConfigProperty(name = "device-provisioning")
    String deviceProvisioning();
}
