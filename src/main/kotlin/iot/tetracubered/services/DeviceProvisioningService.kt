package iot.tetracubered.services

import io.quarkus.vertx.ConsumeEvent
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.vertx.mutiny.core.eventbus.EventBus
import iot.tetracubered.data.entities.Action
import iot.tetracubered.data.entities.Device
import iot.tetracubered.data.repositories.ActionRepository
import iot.tetracubered.data.repositories.DeviceRepository
import iot.tetracubered.messaging.DeviceMessagingHub
import iot.tetracubered.messaging.payloads.ActionProvisioningPayload
import iot.tetracubered.messaging.payloads.DeviceFeedbackMessage
import iot.tetracubered.messaging.payloads.DeviceProvisioningPayload
import org.slf4j.LoggerFactory
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DeviceProvisioningService(
    private val deviceRepository: DeviceRepository,
    private val actionRepository: ActionRepository,
    private val eventBus: EventBus
) {

    private val logger = LoggerFactory.getLogger(DeviceProvisioningService::class.java)

    @ConsumeEvent("device-provisioning")
    fun handleDeviceProvisioning(deviceProvisioningPayload: DeviceProvisioningPayload) {
        logger.info("Checking if device id ${deviceProvisioningPayload.circuitId} exists")
        this.deviceRepository.deviceExistsByCircuitId(circuitId = deviceProvisioningPayload.circuitId)
            .flatMap { existsResponse -> this.getDeviceOrCreate(deviceProvisioningPayload, existsResponse) }
            .flatMap { device -> this.manageActionsProvisioning(device, deviceProvisioningPayload.actions) }
            .subscribe()
            .with {
                logger.info("Device provisioning subscribed")
                this.eventBus.publish("send-feedback",
                    DeviceFeedbackMessage(
                        deviceProvisioningPayload.circuitId,
                        deviceProvisioningPayload.feedbackTopic,
                        true
                    )
                )
            }
    }

    private fun getDeviceOrCreate(deviceProvisioningPayload: DeviceProvisioningPayload, exists: Boolean): Uni<Device> =
        if (exists) {
            this.deviceRepository.getDeviceByCircuitId(deviceProvisioningPayload.circuitId)
        } else {
            this.deviceRepository.createDevice(
                Device(
                    UUID.randomUUID(),
                    deviceProvisioningPayload.circuitId,
                    deviceProvisioningPayload.name,
                    false,
                    deviceProvisioningPayload.feedbackTopic,
                    deviceProvisioningPayload.queryingTopic,
                    "",
                    null,
                    emptyList()
                )
            )
        }

    private fun manageActionsProvisioning(device: Device, actions: List<ActionProvisioningPayload>) =
        Multi.createFrom().items(actions.stream())
            .flatMap { actionProvisioning ->
                this.actionRepository.actionExistsByActionId(device.circuitId, actionProvisioning.actionId)
                    .flatMap { exists -> this.createOrGetAction(device, actionProvisioning, exists) }
                    .toMulti()
            }
            .collectItems().asList()

    private fun createOrGetAction(device: Device, action: ActionProvisioningPayload, exists: Boolean): Uni<Action> =
        if (exists) {
            this.actionRepository.getActionByActionId(device.id, action.actionId)
        } else {
            this.actionRepository.saveAction(
                Action(
                    UUID.randomUUID(),
                    action.actionId,
                    action.name,
                    action.commandTopic,
                    null,
                    device.id
                )
            )
        }
}