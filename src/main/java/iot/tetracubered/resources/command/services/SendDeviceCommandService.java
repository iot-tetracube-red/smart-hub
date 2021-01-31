package iot.tetracubered.resources.command.services;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import iot.tetracubered.data.repositories.ActionRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

@ApplicationScoped
public class SendDeviceCommandService {

    @Inject
    ActionRepository actionRepository;

    @Inject
    EventBus eventBus;

    public Uni<Boolean> runDeviceFeatureAction(String deviceName,
                                               String featureName,
                                               String actionName) {
        return this.actionRepository.getActionTriggerTopicByName(deviceName, featureName, actionName)
                .flatMap(triggerTopic -> {
                    if (triggerTopic == null) {
                        throw new NotFoundException("Cannot found device feature");
                    }
                    return this.eventBus.<Boolean>request("query-action", triggerTopic)
                            .map(Message::body);
                });
    }
}
