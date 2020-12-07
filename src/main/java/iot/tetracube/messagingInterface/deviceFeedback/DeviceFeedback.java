package iot.tetracube.messagingInterface.deviceFeedback;


import iot.tetracube.configurations.SmartHubConfig;
import iot.tetracube.messagingInterface.RabbitMQInterface;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class DeviceFeedback {

    private final RabbitMQInterface rabbitMQClient;
    private final SmartHubConfig smartHubConfig;

    public DeviceFeedback(RabbitMQInterface rabbitMQClient,
                          SmartHubConfig smartHubConfig) {
        this.rabbitMQClient = rabbitMQClient;
        this.smartHubConfig = smartHubConfig;
    }

    public void sendDeviceFeedback(UUID deviceId, boolean feedback) {
        var topicName = this.smartHubConfig.queues().deviceFeedbackTopic()
                .replace("{device_id}", deviceId.toString());
        var message = feedback ? "1" : "0";
      /*  try {
         /*   this.rabbitMQClient.getRabbitMQChannel().basicPublish(
                    this.smartHubConfig.mqttBroker().mqttExchange(),
                    topicName,
                    null,
                    message.getBytes()
            );
        } catch (IOException e) {
            return;
        }*/
    }
}

