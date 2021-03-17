package iot.tetracubered.bot

import io.vertx.core.logging.LoggerFactory
import iot.tetracubered.bot.payloads.DeviceFeature
import iot.tetracubered.data.repositories.DeviceRepository
import iot.tetracubered.data.repositories.FeatureRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class BotServices(
    private val deviceRepository: DeviceRepository,
    private val featureRepository: FeatureRepository
) {

    private val logger = LoggerFactory.getLogger(BotServices::class.java)

    suspend fun getDevices(): List<DeviceFeature> {
        val devices = this.deviceRepository.getAllDevices()
        logger.info("Found ${devices.count()} devices")
        return devices.flatMap { device ->
            val deviceFeatures = this.featureRepository.getDeviceFeatures(device.id)
            logger.info("Found ${deviceFeatures.count()} for the device ${device.id}")
            deviceFeatures.map { deviceFeature ->
                DeviceFeature(
                    device.name,
                    deviceFeature.name
                )
            }
        }
    }
}