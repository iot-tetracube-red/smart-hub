package iot.tetracubered.bot

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.tuples.Tuple2
import iot.tetracubered.bot.payloads.DeviceFeature
import iot.tetracubered.data.repositories.DeviceRepository
import iot.tetracubered.data.repositories.FeatureRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class BotServices(
    private val deviceRepository: DeviceRepository,
    private val featureRepository: FeatureRepository
) {

    fun getDevices(): Multi<DeviceFeature> {
        val devicesMulti = this.deviceRepository.getAllDevices()
        val deviceFeatureTupleMulti = devicesMulti.flatMap { device ->
            this.featureRepository.getDeviceFeatures(device.id)
                .map { feature -> Tuple2.of(device, feature) }
        }
        return deviceFeatureTupleMulti.map { deviceFeatureTuple ->
            val device = deviceFeatureTuple.item1
            val feature = deviceFeatureTuple.item2
            DeviceFeature(
                device.name,
                feature.name
            )
        }
    }
}