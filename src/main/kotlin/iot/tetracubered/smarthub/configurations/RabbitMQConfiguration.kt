package iot.tetracubered.smarthub.configurations

import iot.tetracubered.smarthub.configurations.properties.SmartHubProperties
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfiguration(val smartHubProperties: SmartHubProperties) {

    @Bean
    fun topicExchange() = TopicExchange("amq.topic")

    @Bean
    fun deviceProvisioningQueue(): Queue = Queue(this.smartHubProperties.deviceProvisioningQueueName, false)

    @Bean
    fun deviceProvisioningBinding(provisioningQueue: Queue, topicExchange: TopicExchange): Binding =
        BindingBuilder.bind(provisioningQueue).to(topicExchange)
            .with(this.smartHubProperties.deviceProvisioningTopicName)
}