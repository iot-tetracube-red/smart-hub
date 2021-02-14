package iot.tetracubered.smarthub.data.repositories

import iot.tetracubered.smarthub.data.entities.Action
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ActionRepository : ReactiveCrudRepository<Action, UUID> {
}