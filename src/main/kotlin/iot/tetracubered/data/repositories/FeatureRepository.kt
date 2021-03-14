package iot.tetracubered.data.repositories

import io.smallrye.mutiny.Uni
import io.vertx.mutiny.pgclient.PgPool
import io.vertx.mutiny.sqlclient.Tuple
import iot.tetracubered.data.entities.Feature
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FeatureRepository(private val pgPool: PgPool) {

    fun getFeatureById(featureId: UUID): Uni<Feature?> {
        val query = """
           select *
            from features
            where id = $1
        """
        val params = Tuple.of(featureId)
        return this.pgPool.preparedQuery(query).execute(params)
            .map { rows -> rows.iterator() }
            .map { rowIterator -> if (rowIterator.hasNext()) rowIterator.next() else null  }
            .map { row -> if (row != null) Feature(row) else null }
    }
}