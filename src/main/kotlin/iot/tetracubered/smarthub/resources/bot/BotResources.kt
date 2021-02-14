package iot.tetracubered.smarthub.resources.bot

import iot.tetracubered.smarthub.data.entities.Feature
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/bot")
class BotResources(private val botBusinessServices: BotBusinessServices) {

    @GetMapping(path = ["/features"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getFeatures(): Flux<Feature> {
        return this.botBusinessServices.getFeatures()
    }
}