package iot.tetracube.messagingInterface.deviceProvisioning;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.mutiny.rabbitmq.RabbitMQClient;
import io.vertx.mutiny.rabbitmq.RabbitMQMessage;
import iot.tetracube.configurations.SmartHubConfig;
import iot.tetracube.data.entities.Action;
import iot.tetracube.data.entities.Device;
import iot.tetracube.data.repositories.ActionRepository;
import iot.tetracube.data.repositories.DeviceRepository;
import iot.tetracube.messagingInterface.deviceFeedback.DeviceFeedbackProducer;
import iot.tetracube.messagingInterface.deviceProvisioning.dto.ManageDeviceProvisioningResponse;
import iot.tetracube.messagingInterface.deviceProvisioning.payloads.DeviceActionProvisioningMessage;
import iot.tetracube.messagingInterface.deviceProvisioning.payloads.DeviceProvisioningMessage;
import reactor.core.publisher.Mono;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class DeviceProvisioningMessageHandlerService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceProvisioningMessageHandlerService.class);

    private final Jsonb jsonb;
    private final SmartHubConfig smartHubConfig;
    private final DeviceRepository deviceRepository;
    private final ActionRepository actionRepository;
    private final DeviceFeedbackProducer deviceFeedbackProducer;

    public DeviceProvisioningMessageHandlerService(Jsonb jsonb,
                                                   SmartHubConfig smartHubConfig,
                                                   DeviceRepository deviceRepository,
                                                   ActionRepository actionRepository,
                                                   DeviceFeedbackProducer deviceFeedbackProducer) {
        this.jsonb = jsonb;
        this.smartHubConfig = smartHubConfig;
        this.deviceRepository = deviceRepository;
        this.actionRepository = actionRepository;
        this.deviceFeedbackProducer = deviceFeedbackProducer;
    }

    public void setupDeviceProvisioningQueueListener(RabbitMQClient brokerClient) {
        brokerClient.basicConsumer(this.smartHubConfig.queues().deviceProvisioningQueue())
                .onItem()
                .transform(mqConsumer -> mqConsumer.handler(this::messageCallback))
                .subscribe()
                .with(subscribeCallback -> {
                    LOGGER.info("Subscribed to events chain");
                });
    }

    private void messageCallback(RabbitMQMessage rabbitMQMessage) {
        var message = new String(rabbitMQMessage.body().getBytes());
        LOGGER.info("Provisioning message arrived " + message);
        this.manageDeviceProvisioningMessage(message)
                .onItem()
                .invoke(provisioningResult ->
                        this.deviceFeedbackProducer.sendDeviceFeedback(
                                provisioningResult.getCircuitId(),
                                provisioningResult.getSuccess()
                        )
                )
                .subscribe()
                .with(deviceCallback ->
                        LOGGER.info("Subscribed to database operations chains"));
    }

    private Uni<ManageDeviceProvisioningResponse> manageDeviceProvisioningMessage(String message) {
        LOGGER.info("Parsing device provisioning message");
        DeviceProvisioningMessage deviceProvisioningMessage;
        try {
            deviceProvisioningMessage = this.jsonb.fromJson(message, DeviceProvisioningMessage.class);
        } catch (Exception e) {
            LOGGER.error("Cannot convert message, ignoring it");
            return Uni.createFrom().nullItem();
        }

        LOGGER.info("Checking if device already exists");
        return this.deviceRepository.deviceExistsByCircuitId(deviceProvisioningMessage.getId())
                .flatMap(deviceExists -> {
                    LOGGER.info("Save or get existing device");
                    if (deviceExists == null) {
                        LOGGER.error("There was some error during device verification, returning bad feedback to device");
                        return Uni.createFrom().nullItem();
                    } else if (deviceExists) {
                        LOGGER.info("Device exists, updating only its actions if needed");
                        return this.deviceRepository.getDeviceByCircuitId(deviceProvisioningMessage.getId());
                    } else {
                        LOGGER.info("Storing device data in database");
                        var deviceEntity = new Device(
                                UUID.randomUUID(),
                                deviceProvisioningMessage.getId(),
                                deviceProvisioningMessage.getDefaultName(),
                                false,
                                deviceProvisioningMessage.getHostname()
                        );
                        return this.deviceRepository.createDevice(deviceEntity);
                    }
                })
                .flatMap(device ->
                        Multi.createFrom().items(deviceProvisioningMessage.getDeviceActionProvisioningMessages().stream())
                                .onItem()
                                .transformToMulti(deviceAction -> this.deviceActionProvisioning(device.getId(), deviceAction))
                                .concatenate().collectItems().asList()
                                .map(results -> results.stream().allMatch(result -> result))
                                .map(result -> new ManageDeviceProvisioningResponse(device.getCircuitId(), result))
                );
    }

    private Mono<Boolean> deviceActionProvisioning(UUID deviceId, DeviceActionProvisioningMessage deviceActionProvisioningMessage) {
        var actionExistsUni = this.actionRepository.existsActionByActionId(deviceActionProvisioningMessage.getId());
        var resultUni = actionExistsUni.flatMap(deviceExists -> {
            if (deviceExists) {
                LOGGER.info("Device exists - ignoring registration");
                return Uni.createFrom().item(true);
            }
            var action = new Action(
                    UUID.randomUUID(),
                    deviceActionProvisioningMessage.getId(),
                    deviceActionProvisioningMessage.getDefaultName(),
                    false,
                    deviceActionProvisioningMessage.getTopic(),
                    null,
                    deviceId
            );
            return this.actionRepository.createAction(action)
                    .map(Optional::isPresent);
        });
        return resultUni.convert().with(UniReactorConverters.toMono());
    }
}
