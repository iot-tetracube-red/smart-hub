package iot.tetracube.deviceComunication.deviceProvisioning;

import com.rabbitmq.client.DeliverCallback;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.mutiny.core.eventbus.EventBus;
import iot.tetracube.deviceComunication.feedback.DeviceFeedback;
import iot.tetracube.broker.RabbitMQClient;
import iot.tetracube.configurations.SmartHubConfig;
import iot.tetracube.models.dto.ManageDeviceProvisioningResponse;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Singleton
@ApplicationScoped
public class DeviceProvisioningQueueConsumer {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceProvisioningQueueConsumer.class);

    private final EventBus eventBus;
    private final SmartHubConfig smartHubConfig;
    private final RabbitMQClient rabbitMQClient;
    private final DeviceFeedback deviceFeedback;

    public DeviceProvisioningQueueConsumer(SmartHubConfig smartHubConfig,
                                           RabbitMQClient rabbitMQClient,
                                           EventBus eventBus,
                                           DeviceFeedback deviceFeedback) {
        this.smartHubConfig = smartHubConfig;
        this.rabbitMQClient = rabbitMQClient;
        this.eventBus = eventBus;
        this.deviceFeedback = deviceFeedback;
    }

    public void startup(@Observes StartupEvent startupEvent) {
        try {
            this.setupDeviceProvisioningQueueListener();
        } catch (IOException e) {
            LOGGER.error("Cannot consume queues. All services will be offline.");
        }
    }

    public void setupDeviceProvisioningQueueListener() throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            final var message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            eventBus.<ManageDeviceProvisioningResponse>request("device-provisioning", message)
                    .subscribe()
                    .with(result -> {
                        if (result.body() != null) {
                            LOGGER.info("Sending feedback to device");
                            this.deviceFeedback.sendDeviceFeedback(result.body().getCircuitId(), result.body().getSuccess());
                        }
                    });
        };

        this.rabbitMQClient.getRabbitMQChannel().basicConsume(
                this.smartHubConfig.queues().deviceProvisioningQueue(),
                true,
                deliverCallback,
                consumerTag -> {
                }
        );
    }

}
