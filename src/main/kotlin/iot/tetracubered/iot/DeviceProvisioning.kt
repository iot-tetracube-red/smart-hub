package iot.tetracubered.iot

import com.fasterxml.jackson.databind.ObjectMapper
import iot.tetracubered.iot.payloads.DeviceProvisioningPayload
import javax.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Incoming
import java.lang.Exception
import iot.tetracubered.data.entities.Device

import io.smallrye.mutiny.Uni
import iot.tetracubered.data.repositories.DeviceRepository
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.tuples.Tuple;
import io.smallrye.mutiny.tuples.Tuple2
import io.vertx.core.logging.LoggerFactory
import iot.tetracubered.data.entities.Feature
import iot.tetracubered.data.repositories.FeatureRepository

import iot.tetracubered.iot.payloads.DeviceFeatureProvisioningPayload


@ApplicationScoped
class DeviceProvisioning(
    private val objectMapper: ObjectMapper,
    private val deviceRepository: DeviceRepository,
    private val featureRepository: FeatureRepository
) {

    private val logger = LoggerFactory.getLogger(DeviceProvisioning::class.java)

    @Incoming("devices-provisioning")
    fun deviceProvisioning(payload: ByteArray?) {
        logger.info("Arrived message in device-provisioning topic: " + String(payload!!))
        val deviceProvisioningPayload: DeviceProvisioningPayload = try {
            this.objectMapper.readValue(String(payload), DeviceProvisioningPayload::class.java)
        } catch (ex: Exception) {
            logger.info("Cannot read payload content, ignoring it")
            return
        }
        logger.info("Trying to store device with circuit id: " + deviceProvisioningPayload.id)
        this.storeDevice(deviceProvisioningPayload)
            .subscribe()
            .with { storedDevice ->
                logger.info("Device stored with id ${storedDevice.id}")
            }
    }

    private fun storeDevice(deviceProvisioningPayload: DeviceProvisioningPayload): Uni<Device> {
        logger.info("Check if device exists giving CircuitId")
        return this.deviceRepository.getDeviceById(deviceProvisioningPayload.id)
            .flatMap { dbDevice ->
                if (dbDevice == null) {
                    val deviceToStore = Device(
                        deviceProvisioningPayload.id,
                        deviceProvisioningPayload.name,
                        deviceProvisioningPayload.feedbackTopic
                    )
                    this.deviceRepository.storeDevice(deviceToStore)
                } else {
                    val updatedDevice = dbDevice.copy(
                        feedbackTopic = deviceProvisioningPayload.feedbackTopic,
                        isOnline = true
                    )
                    this.deviceRepository.updateDevice(updatedDevice)
                }
            }
        // .call { device -> this.storeFeatures(device, deviceProvisioningPayload.features) }
    }

    /*      private fun storeFeatures(
        device: Device,
        featuresPayload: List<DeviceFeatureProvisioningPayload>
    ): Uni<List<Feature>> {
        return Multi.createFrom().items(featuresPayload.parallelStream())
            .flatMap { feature ->
                this.featureRepository.getFeatureById(feature.featureId)
                    .map { dbFeature -> Tuple2.of(feature, dbFeature) }
                    .toMulti()
            }
  return Multi.createFrom().items(featuresPayload.parallelStream())
            .flatMap<Any> { feature: DeviceFeatureProvisioningPayload ->
                this.featureRepository.getFeatureByFeatureId(feature.featureId, device.id)
                    .map { dbFeature -> Tuple2.of(feature, dbFeature) }
                    .onFailure().recoverWithItem(Tuple2.of(feature, null))
                    .toMulti()
            }
            .flatMap<Any> { featureTuple: Any ->
                val featureProvisioning: Unit = featureTuple.getItem1()
                val dbFeature: Unit = featureTuple.getItem2()
                if (featureTuple.getItem2() == null) {
                    val newFeature = Feature(
                        featureProvisioning.getFeatureId(),
                        featureProvisioning.getName(),
                        featureProvisioning.getFeatureType(),
                        featureProvisioning.getValue(),
                        device
                    )
                    return@flatMap this.featureRepository.storeFeature(newFeature)
                        .map { storedFeature -> Tuple2.of(storedFeature, featureProvisioning.getActions()) }
                        .toMulti()
                }
                dbFeature.setFeatureType(featureProvisioning.getFeatureType())
                dbFeature.setCurrentValue(featureProvisioning.getValue())
                dbFeature.setRunning(false)
                dbFeature.setSourceReferenceId(null)
                dbFeature.setSourceType(null)
                this.featureRepository.updateFeature(dbFeature)
                    .map { storedFeature -> Tuple2.of(storedFeature, featureProvisioning.getActions()) }
                    .toMulti()
            }
            .call(Function<Any, Uni<*>> { featureActionsTuple: Any ->
                this.storeAction(
                    featureActionsTuple.getItem1(),
                    featureActionsTuple.getItem2()
                )
            })
            .map<Any>(Tuple2::getItem1)
            .collect().asList()
    }*/
}