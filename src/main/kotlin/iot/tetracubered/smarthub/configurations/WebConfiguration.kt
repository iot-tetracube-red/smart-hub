package iot.tetracubered.smarthub.configurations

import iot.tetracubered.smarthub.data.repositories.DeviceRepository
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@Configuration
@ConfigurationPropertiesScan
@EnableReactiveMongoRepositories(basePackageClasses = [
    DeviceRepository::class
])
class WebConfiguration {
}