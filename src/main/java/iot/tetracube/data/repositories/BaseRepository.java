package iot.tetracube.data.repositories;

import io.smallrye.mutiny.Uni;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Query;
import org.neo4j.driver.Record;

public class BaseRepository {

    protected Uni<Record> executeQuery(Driver driver, Query query) {
        return Uni.createFrom().publisher(driver.rxSession().beginTransaction())
                .flatMap(rxTransaction -> {
                    return Uni.createFrom().publisher(rxTransaction.run(query).records() )
                            .flatMap(x -> rxTransaction.commit());
                } ).map(c -> c.);
    }

}
