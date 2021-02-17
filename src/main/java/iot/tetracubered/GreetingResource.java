package iot.tetracubered;

import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.smallrye.mutiny.Multi;
import iot.tetracubered.data.entities.Action;
import iot.tetracubered.data.repositories.ActionRepository;

@Path("/hello-resteasy")
public class GreetingResource {

    @Inject
    ActionRepository actionRepository;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Multi<Action> hello() {
        return actionRepository.getDeviceActions(UUID.randomUUID());
    }
}