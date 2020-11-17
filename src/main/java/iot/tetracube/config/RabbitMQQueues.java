package iot.tetracube.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public interface RabbitMQQueues {

    @ConfigProperty(name = "device-provisioning-queue")
    String deviceProvisioningQueue();

    @ConfigProperty(name = "device-provisioning-topic")
    String deviceProvisioningTopic();

    @ConfigProperty(name = "device-feedback-topic")
    String deviceFeedbackTopic();

}
