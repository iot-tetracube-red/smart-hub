package iot.tetracubered.smarthub.data.repositories

import iot.tetracubered.smarthub.data.entities.Feature
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FeatureRepository : ReactiveCrudRepository<Feature, UUID> {
}