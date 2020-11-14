package iot.tetracube.broker;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import iot.tetracube.config.SmartHubConfig;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Singleton
@ApplicationScoped
public class RabbitMQClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(RabbitMQClient.class);

    private final SmartHubConfig smartHubConfig;
    private Channel rabbitMQChannel;

    public RabbitMQClient(SmartHubConfig smartHubConfig) {
        this.smartHubConfig = smartHubConfig;
        try {
            this.initializeConnection();
        } catch (IOException | TimeoutException e) {
            LOGGER.error("Cannot establish RabbitMQ connection. All services will be offline.");
        }
    }

    private void initializeConnection() throws IOException, TimeoutException {
        LOGGER.info("Setting up RabbitMQ connection options");
        var rabbitMQConnectionFactory = new ConnectionFactory();
        rabbitMQConnectionFactory.setHost(this.smartHubConfig.mqttBroker().host());
        rabbitMQConnectionFactory.setPort(this.smartHubConfig.mqttBroker().port());
        rabbitMQConnectionFactory.setUsername(this.smartHubConfig.mqttBroker().username());
        rabbitMQConnectionFactory.setPassword(this.smartHubConfig.mqttBroker().password());

        LOGGER.info("Connecting to RabbitMQ broker");
        var rabbitMQConnection = rabbitMQConnectionFactory.newConnection();
        this.rabbitMQChannel = rabbitMQConnection.createChannel();
    }

}
