package iot.tetracubered.iot

import com.fasterxml.jackson.databind.ObjectMapper
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.tuples.Tuple2
import io.vertx.core.logging.LoggerFactory
import io.vertx.mutiny.core.eventbus.EventBus
import iot.tetracubered.data.entities.Action
import iot.tetracubered.data.entities.Device
import iot.tetracubered.data.entities.Feature
import iot.tetracubered.data.repositories.ActionRepository
import iot.tetracubered.data.repositories.DeviceRepository
import iot.tetracubered.data.repositories.FeatureRepository
import iot.tetracubered.iot.payloads.DeviceFeatureActionProvisioningPayload
import iot.tetracubered.iot.payloads.DeviceFeatureProvisioningPayload
import iot.tetracubered.iot.payloads.DeviceProvisioningPayload
import org.eclipse.microprofile.reactive.messaging.Incoming
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class DeviceProvisioning(
    private val objectMapper: ObjectMapper,
    private val deviceRepository: DeviceRepository,
    private val featureRepository: FeatureRepository,
    private val actionRepository: ActionRepository,
    private val eventBus: EventBus
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
    }

    private fun storeDevice(deviceProvisioningPayload: DeviceProvisioningPayload) {
        logger.info("Check if device exists giving CircuitId")
        this.deviceRepository.getDeviceById(deviceProvisioningPayload.id)
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
            .subscribe()
            .with { device ->
                this.storeFeatures(device, deviceProvisioningPayload.features)
                logger.info("Device stored with id ${device.id}")
            }
    }

    private fun storeFeatures(device: Device, featuresToStore: List<DeviceFeatureProvisioningPayload>) {
        val featuresMulti = Multi.createFrom().items(featuresToStore.parallelStream())
        val dbFeatureMulti = featuresMulti.flatMap { feature ->
            this.featureRepository.getFeatureById(feature.featureId)
                .map { dbFeature -> Tuple2.of(feature, dbFeature) }
                .toMulti()
        }
        val updatedDbFeatureMulti = dbFeatureMulti.flatMap { featureTuple ->
            val featureProvisioning = featureTuple.item1
            val dbFeature = featureTuple.item2
            val dbFeatureUni = if (dbFeature == null) {
                val newFeature = Feature(
                    id = featureProvisioning.featureId,
                    name = featureProvisioning.name,
                    featureType = featureProvisioning.featureType,
                    currentValue = featureProvisioning.value,
                    isRunning = false,
                    deviceId = device.id
                )
                this.featureRepository.storeFeature(newFeature)
            } else {
                val updatedFeature = dbFeature.copy(
                    featureType = featureProvisioning.featureType,
                    currentValue = featureProvisioning.value,
                    isRunning = false,
                    runningReferenceId = null,
                    sourceType = null
                )
                this.featureRepository.updateFeature(updatedFeature)
            }
            dbFeatureUni.map { storedFeature -> Tuple2.of(storedFeature, featureProvisioning.actions) }
                .toMulti()
        }
        updatedDbFeatureMulti.subscribe()
            .with { featureTuple ->
                logger.info("Feature id ${featureTuple.item1.id} managed correctly")
                this.storeAction(featureTuple.item1, featureTuple.item2)
            }
    }

    private fun storeAction(feature: Feature, actionsToStore: List<DeviceFeatureActionProvisioningPayload>) {
        val actionToStoreMulti = Multi.createFrom().items(actionsToStore.parallelStream())
        val dbActionMulti = actionToStoreMulti.flatMap { actionToStore ->
            this.actionRepository.getActionById(actionToStore.actionId)
                .map { action -> Tuple2.of(actionToStore, action) }
                .toMulti()
        }
        val updatedDbAction = dbActionMulti.flatMap { actionTuple ->
            val actionProvisioning = actionTuple.item1
            val dbAction = actionTuple.item2
            val dbActionUni = if (dbAction == null) {
                val actionToStore = Action(
                    id = actionProvisioning.actionId,
                    featureId = feature.id,
                    name = actionProvisioning.name,
                    triggerTopic = actionProvisioning.triggerTopic
                )
                this.actionRepository.storeAction(actionToStore)
            } else {
                val updatedAction = dbAction.copy(
                    triggerTopic = actionProvisioning.triggerTopic
                )
                this.actionRepository.updateAction(updatedAction)
            }
            dbActionUni.toMulti()
        }
        updatedDbAction.subscribe()
            .with { action ->
                logger.info("Action id ${action.id} managed correctly")
            }
    }
}