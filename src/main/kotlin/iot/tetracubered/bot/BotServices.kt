package iot.tetracubered.bot

import io.vertx.core.logging.LoggerFactory
import iot.tetracubered.bot.payloads.DeviceFeature
import iot.tetracubered.bot.payloads.DeviceFeatureAction
import iot.tetracubered.data.repositories.ActionRepository
import iot.tetracubered.data.repositories.DeviceRepository
import iot.tetracubered.data.repositories.FeatureRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class BotServices(
    private val deviceRepository: DeviceRepository,
    private val featureRepository: FeatureRepository,
    private val actionRepository: ActionRepository
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

    suspend fun getDeviceFeatureActions(deviceName: String, featureName: String): DeviceFeatureAction? {
        val device = this.deviceRepository.getDeviceByName(deviceName) ?: return null
        val feature = this.featureRepository.getFeatureByDeviceAndName(device.id, featureName) ?: return null
        val actions = this.actionRepository.getFeatureActions(feature.id)
            .map { action -> action.name }
        return DeviceFeatureAction(
            device.name,
            feature.name,
            actions
        )
    }
}