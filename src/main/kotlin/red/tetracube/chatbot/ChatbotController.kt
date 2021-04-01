package red.tetracube.chatbot

import io.smallrye.mutiny.Uni
import red.tetracube.database.entities.Device
import red.tetracube.database.repositories.DeviceRepository
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType.APPLICATION_JSON

@Path("/api/bot")
class ChatbotController(private val deviceRepository: DeviceRepository) {

    @GET
    @Path("/test")
    @Produces(APPLICATION_JSON)
    fun createDeviceTest(): Uni<Device> {

        return this.deviceRepository.find("features.featureId", "b5259357-30c2-41cf-9a0f-a5dec463039c")
                .firstResultOptional<Device>()
                .map { savedDevice ->
                    if (savedDevice.isEmpty) {
                        null
                    } else {
                        savedDevice.get()
                    }
                }
    }
}