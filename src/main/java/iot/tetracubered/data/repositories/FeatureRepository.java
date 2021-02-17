package iot.tetracubered.data.repositories;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.hibernate.reactive.mutiny.Mutiny.Session;

@ApplicationScoped
public class FeatureRepository {

    @Inject
    Session hibernateReactiveSession;
}
