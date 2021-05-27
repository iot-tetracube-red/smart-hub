package red.tetracube.smarthub.iot.services;

import io.smallrye.mutiny.Uni;
import red.tetracube.smarthub.data.repositories.FeatureRepository;
import red.tetracube.smarthub.iot.dto.ActionTriggerMessage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TriggerActionDataService {

    @Inject
    FeatureRepository featureRepository;

    public Uni<Boolean> storeTrigger(ActionTriggerMessage actionTriggerMessage) {
        return featureRepository.getFeatureById(actionTriggerMessage.featureId())
                .flatMap(optionalFeature -> {
                   if (optionalFeature.isEmpty()) {
                       return Uni.createFrom().item(false);
                   }
                   var updatedFeature = optionalFeature.get();
                   updatedFeature.setRunning(true);
                   updatedFeature.setSourceType(actionTriggerMessage.sourceType());
                   updatedFeature.setRunningReferenceId(actionTriggerMessage.sourceId());
                   return featureRepository.insertOrUpdateFeature(updatedFeature)
                           .map(ignored -> true)
                           .onFailure()
                           .recoverWithItem(false);
                });
    }
}
