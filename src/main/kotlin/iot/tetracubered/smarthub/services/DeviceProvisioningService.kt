package iot.tetracubered.smarthub.services

import iot.tetracubered.smarthub.data.entities.Action
import iot.tetracubered.smarthub.data.entities.Device
import iot.tetracubered.smarthub.data.repositories.DeviceRepository
import iot.tetracubered.smarthub.messaging.payloads.DeviceActionProvisioning
import iot.tetracubered.smarthub.messaging.payloads.DeviceProvisioning
import kotlinx.coroutines.reactive.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class DeviceProvisioningService(private val deviceRepository: DeviceRepository) {

    private val logger = LoggerFactory.getLogger(DeviceProvisioningService::class.java)

    suspend fun manageDeviceProvisioning(deviceProvisioning: DeviceProvisioning) {
        this.logger.info("Checking if device already exists")
        val deviceExists = this.deviceRepository.existsById(deviceProvisioning.circuitId).awaitSingle()
        val device = if (deviceExists) {
            this.logger.info("Exists... getting and processing the actions")
            this.deviceRepository.findById(deviceProvisioning.circuitId).awaitSingle()
        } else {
            this.logger.info("Does not exists... creating a new one")
            val newDevice = Device(
                deviceProvisioning.circuitId,
                deviceProvisioning.name,
                true,
                deviceProvisioning.feedbackTopic,
                null,
                ArrayList()
            )
            this.deviceRepository.save(newDevice).awaitSingle()
        }
        this.logger.info("Updating feedback topic")
        val updatedDevice = device.copy(
            feedbackTopic = deviceProvisioning.feedbackTopic
        )
        this.logger.info("Updating actions")
        val updatedActionsDevice = this.mapDeviceAction(updatedDevice, deviceProvisioning.actions)
        this.logger.info("Save device updates")
        this.deviceRepository.save(updatedActionsDevice).awaitSingle()
    }

    suspend fun mapDeviceAction(device: Device, deviceProvisioningActions: List<DeviceActionProvisioning>): Device {
        val updatedActions = deviceProvisioningActions.stream()
            .map { deviceActionProvisioning ->
                this.logger.info("Check if device already contains action ${deviceActionProvisioning.defaultName}")
                val deviceAction = device.actions.stream()
                    .filter { a -> a.actionId == deviceActionProvisioning.actionId }
                    .findFirst()
                    .orElse(
                        Action(
                            deviceActionProvisioning.actionId,
                            deviceActionProvisioning.value,
                            deviceActionProvisioning.commandTopic,
                            deviceActionProvisioning.queryingTopic,
                            null
                        )
                    )
                this.logger.info("Updating action with the updated value and topics")
                deviceAction.copy(
                    commandTopic = deviceActionProvisioning.commandTopic,
                    queryingTopic = deviceActionProvisioning.queryingTopic,
                    currentValue = deviceActionProvisioning.value
                )
            }
            .collect(Collectors.toList())
        this.logger.info("Copy the new list with the old one")
        return device.copy(actions = updatedActions)
    }
}