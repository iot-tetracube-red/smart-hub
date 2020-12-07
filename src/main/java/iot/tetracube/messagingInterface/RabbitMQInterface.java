package iot.tetracube.messagingInterface;

import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.core.Vertx;
import io.vertx.rabbitmq.RabbitMQOptions;
import io.vertx.mutiny.rabbitmq.RabbitMQClient;
import iot.tetracube.configurations.SmartHubConfig;
import iot.tetracube.messagingInterface.deviceProvisioning.DeviceProvisioningMessageHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Singleton
@ApplicationScoped
public class RabbitMQInterface {

    private final static Logger LOGGER = LoggerFactory.getLogger(RabbitMQInterface.class);

    private final SmartHubConfig smartHubConfig;
    private final Vertx vertx;
    private RabbitMQClient brokerClient;
    private final DeviceProvisioningMessageHandlerService deviceProvisioningMessageHandlerService;

    public RabbitMQInterface(SmartHubConfig smartHubConfig,
                             Vertx vertx,
                             DeviceProvisioningMessageHandlerService deviceProvisioningMessageHandlerService) {
        this.smartHubConfig = smartHubConfig;
        this.vertx = vertx;
        this.deviceProvisioningMessageHandlerService = deviceProvisioningMessageHandlerService;
    }

    public void startup(@Observes StartupEvent startupEvent) {
        try {
            this.initializeConnection();
        } catch (IOException | TimeoutException e) {
            LOGGER.error("Cannot establish RabbitMQ connection. All services will be offline.");
        }
    }

    private void initializeConnection() throws IOException, TimeoutException {
        LOGGER.info("Setting up RabbitMQ connection options");
        var rabbitMQOptions = new RabbitMQOptions();
        rabbitMQOptions.setHost(this.smartHubConfig.mqttBroker().host());
        rabbitMQOptions.setPort(this.smartHubConfig.mqttBroker().port());
        rabbitMQOptions.setUser(this.smartHubConfig.mqttBroker().username());
        rabbitMQOptions.setPassword(this.smartHubConfig.mqttBroker().password());
        rabbitMQOptions.setVirtualHost("/");
        rabbitMQOptions.setConnectionTimeout(6000); // in milliseconds
        rabbitMQOptions.setRequestedHeartbeat(60); // in seconds
        rabbitMQOptions.setHandshakeTimeout(6000); // in milliseconds
        rabbitMQOptions.setRequestedChannelMax(5);
        rabbitMQOptions.setNetworkRecoveryInterval(500); // in milliseconds
        rabbitMQOptions.setAutomaticRecoveryEnabled(true);

        LOGGER.info("Creating RabbitMQ client");
        this.brokerClient = RabbitMQClient.create(this.vertx, rabbitMQOptions);
        LOGGER.info("Listening to broker connection result");
        this.brokerClient.start().subscribe()
                .with((asyncResult) -> {
                    LOGGER.info("Connection to broker success");
                    try {
                        this.initializeQueues();
                    } catch (IOException ignored) {
                        LOGGER.info("Cannot initialize queues - impossible");
                    }
                    this.deviceProvisioningMessageHandlerService.setupDeviceProvisioningQueueListener(this.brokerClient);
                });
    }

    private void initializeQueues() throws IOException {
        LOGGER.info("Initializing device provisioning queue");
        this.brokerClient.queueDeclare(
                this.smartHubConfig.queues().deviceProvisioningQueue(),
                true,
                false,
                false,
                null
        );

        LOGGER.info("Declaring queue binding with mqtt exchange");
        this.brokerClient.queueBind(
                this.smartHubConfig.queues().deviceProvisioningQueue(),
                this.smartHubConfig.mqttBroker().mqttExchange(),
                this.smartHubConfig.queues().deviceProvisioningTopic(),
                null
        );
    }
}
