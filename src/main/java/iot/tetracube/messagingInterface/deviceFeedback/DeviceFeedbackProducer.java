package iot.tetracube.messagingInterface.deviceFeedback;


import io.vertx.core.json.JsonObject;
import iot.tetracube.configurations.SmartHubConfig;
import iot.tetracube.messagingInterface.RabbitMQInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class DeviceFeedbackProducer {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceFeedbackProducer.class);

    private final RabbitMQInterface rabbitMQClient;
    private final SmartHubConfig smartHubConfig;

    public DeviceFeedbackProducer(RabbitMQInterface rabbitMQClient,
                                  SmartHubConfig smartHubConfig) {
        this.rabbitMQClient = rabbitMQClient;
        this.smartHubConfig = smartHubConfig;
    }

    public void sendDeviceFeedback(UUID deviceId, Boolean feedback) {
        var topicName = this.smartHubConfig.queues().deviceFeedbackTopic()
                .replace("{device_id}", deviceId.toString());
        var jsonObject = new JsonObject()
                .put("properties", new JsonObject().put("contentType", "application/json"))
                .put("body", new JsonObject().put("result", feedback.toString()));
        this.rabbitMQClient.getBrokerClient().basicPublish(
                this.smartHubConfig.mqttBroker().mqttExchange(),
                topicName,
                jsonObject
        )
                .subscribe()
                .with(result -> {
                    LOGGER.info("Subscribed to device feedback producing");
                });
    }
}

