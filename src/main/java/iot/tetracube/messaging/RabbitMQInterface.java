package iot.tetracube.messaging;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.TimeoutException;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQOptions;
import iot.tetracube.configurations.SmartHubConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class RabbitMQInterface {

    private final static Logger LOGGER = LoggerFactory.getLogger(RabbitMQInterface.class);

    private final SmartHubConfig smartHubConfig;
    private final Vertx vertx;
    private RabbitMQClient brokerClient;

    public RabbitMQInterface(SmartHubConfig smartHubConfig,
                             Vertx vertx) {
        this.smartHubConfig = smartHubConfig;
        this.vertx = vertx;
    }

    public void startup(@Observes StartupEvent startupEvent) {
        try {
            this.initializeConnection();
            this.brokerClient.start().subscribe()
                    .with((asyncResult) -> {
                        LOGGER.info("Connection to broker success");
                        try {
                            this.initializeQueues();
                        } catch (IOException ignored) {
                            LOGGER.info("Cannot initialize queues - impossible");
                        }
                        //this.deviceProvisioningMessageHandlerService.setupDeviceProvisioningQueueListener(this.brokerClient);
                    });
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
        rabbitMQOptions.setConnectionTimeout(6000);
        rabbitMQOptions.setRequestedHeartbeat(60);
        rabbitMQOptions.setHandshakeTimeout(6000);
        rabbitMQOptions.setRequestedChannelMax(5);
        rabbitMQOptions.setNetworkRecoveryInterval(500);
        rabbitMQOptions.setAutomaticRecoveryEnabled(true);

        LOGGER.info("Creating RabbitMQ client");
        this.brokerClient = RabbitMQClient.create(this.vertx, rabbitMQOptions);
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
