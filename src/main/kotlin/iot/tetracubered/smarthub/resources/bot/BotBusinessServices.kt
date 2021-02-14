package iot.tetracubered.smarthub.resources.bot

import iot.tetracubered.smarthub.data.entities.Feature
import iot.tetracubered.smarthub.data.repositories.FeatureRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class BotBusinessServices(private val featureRepository: FeatureRepository) {

    fun getFeatures(): Flux<Feature> {
        return this.featureRepository.findAll();
    }
}