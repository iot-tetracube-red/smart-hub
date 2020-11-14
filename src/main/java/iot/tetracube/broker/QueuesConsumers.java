package iot.tetracube.broker;

import com.rabbitmq.client.DeliverCallback;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.mutiny.core.eventbus.EventBus;
import iot.tetracube.config.SmartHubConfig;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Singleton
@ApplicationScoped
public class QueuesConsumers {

    private final static Logger LOGGER = LoggerFactory.getLogger(QueuesConsumers.class);

    private final EventBus eventBus;
    private final SmartHubConfig smartHubConfig;
    private final RabbitMQClient rabbitMQClient;

    public QueuesConsumers(SmartHubConfig smartHubConfig,
                           RabbitMQClient rabbitMQClient,
                           EventBus eventBus) {
        this.smartHubConfig = smartHubConfig;
        this.rabbitMQClient = rabbitMQClient;
        this.eventBus = eventBus;
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
            eventBus.sendAndForget("device-provisioning", message);
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
