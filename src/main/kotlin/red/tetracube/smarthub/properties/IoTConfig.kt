package red.tetracube.smarthub.properties;

import org.eclipse.microprofile.config.inject.ConfigProperty

interface IoTConfig {

    @ConfigProperty(name = "broker")
    fun broker(): BrokerConfig

    @ConfigProperty(name = "topics")
    fun topics(): TopicsConfig

}