package iot.tetracube.broker;

import iot.tetracube.config.SmartHubConfig;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.util.UUID;

@ApplicationScoped
public class QueuesProducers {

    private final RabbitMQClient rabbitMQClient;
    private final SmartHubConfig smartHubConfig;

    public QueuesProducers(RabbitMQClient rabbitMQClient,
                           SmartHubConfig smartHubConfig) {
        this.rabbitMQClient = rabbitMQClient;
        this.smartHubConfig = smartHubConfig;
    }

    public void sendDeviceFeedback(UUID deviceId, boolean feedback) {
        var topicName = this.smartHubConfig.queues().deviceFeedbackTopic()
                .replace("{device_id}", deviceId.toString());
        var message = feedback ? "1" : "0";
        try {
            this.rabbitMQClient.getRabbitMQChannel().basicPublish(
                    this.smartHubConfig.mqttBroker().mqttExchange(),
                    topicName,
                    null,
                    message.getBytes()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
