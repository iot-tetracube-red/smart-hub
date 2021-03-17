package iot.tetracubered.iot

import com.fasterxml.jackson.databind.ObjectMapper
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
        GlobalScope.launch {
            storeDevice(deviceProvisioningPayload)
        }
    }

    private suspend fun storeDevice(deviceProvisioningPayload: DeviceProvisioningPayload) {
        logger.info("Check if device exists giving CircuitId")
        val dbDevice = this.deviceRepository.getDeviceById(deviceProvisioningPayload.id)
        val device = if (dbDevice == null) {
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
        this.storeFeatures(device, deviceProvisioningPayload.features)
        logger.info("Device stored with id ${device.id}")
    }

    private suspend fun storeFeatures(device: Device, featuresToStore: List<DeviceFeatureProvisioningPayload>) {
        featuresToStore
            .forEach { feature ->
                val dbFeature = featureRepository.getFeatureById(feature.featureId)
                val dbFeatureUpdated = if (dbFeature == null) {
                    val newFeature = Feature(
                        id = feature.featureId,
                        name = feature.name,
                        featureType = feature.featureType,
                        currentValue = feature.value,
                        isRunning = false,
                        deviceId = device.id
                    )
                    this.featureRepository.storeFeature(newFeature)
                } else {
                    val updatedFeature = dbFeature.copy(
                        featureType = feature.featureType,
                        currentValue = feature.value,
                        isRunning = false,
                        runningReferenceId = null,
                        sourceType = null
                    )
                    this.featureRepository.updateFeature(updatedFeature)
                }
                logger.info("Feature id ${dbFeatureUpdated.id} managed correctly")
                this.storeAction(dbFeatureUpdated, feature.actions)
            }
    }

    private suspend fun storeAction(feature: Feature, actionsToStore: List<DeviceFeatureActionProvisioningPayload>) {
        actionsToStore.forEach { actionToStore ->
            val dbAction = this.actionRepository.getActionById(actionToStore.actionId)
            val updatedDbAction = if (dbAction == null) {
                val newAction = Action(
                    id = actionToStore.actionId,
                    featureId = feature.id,
                    name = actionToStore.name,
                    triggerTopic = actionToStore.triggerTopic
                )
                this.actionRepository.storeAction(newAction)
            } else {
                val updatedAction = dbAction.copy(
                    triggerTopic = actionToStore.triggerTopic
                )
                this.actionRepository.updateAction(updatedAction)
            }
            logger.info("Action id ${updatedDbAction.id} managed correctly")
        }
    }
}