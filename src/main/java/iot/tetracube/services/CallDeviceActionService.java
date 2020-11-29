package iot.tetracube.services;

import io.smallrye.mutiny.Uni;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import iot.tetracube.data.entities.Action;
import iot.tetracube.data.repositories.ActionRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.NoSuchElementException;

@ApplicationScoped
public class CallDeviceActionService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CallDeviceActionService.class);

    private final ActionRepository actionRepository;

    public CallDeviceActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public Uni<Action> callDeviceAction(String actionName) {
        LOGGER.info("Getting action by name " + actionName);
        return this.actionRepository.getActionByName(actionName)
                .onItem()
                .invoke(action -> {
                    if (action == null) {
                        throw new NoSuchElementException("Cannot find action with given name");
                    }
                    LOGGER.info("Got device, publishing message on topic " + action.getTopic());
                });
    }

}
