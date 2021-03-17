package iot.tetracubered.data.repositories

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.mutiny.pgclient.PgPool
import io.vertx.mutiny.sqlclient.Tuple
import iot.tetracubered.data.entities.Feature
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FeatureRepository(private val pgPool: PgPool) {

    suspend fun getFeatureById(featureId: UUID): Feature? {
        val query = """
           select *
            from features
            where id = $1
        """
        val params = Tuple.of(featureId)
        return this.pgPool.preparedQuery(query).execute(params)
            .map { rows -> rows.iterator() }
            .map { rowIterator -> if (rowIterator.hasNext()) rowIterator.next() else null }
            .map { row -> if (row != null) Feature(row) else null }
            .awaitSuspending()
    }

    suspend fun storeFeature(feature: Feature): Feature {
        val query = """
            insert into features(id, name, feature_type, current_value, is_running, source_type, running_reference_id, device_id) 
            values ($1, $2, $3, $4, $5, $6, $7, $8)
        """
        val params = Tuple.of(
            feature.id,
            feature.name,
            feature.featureType.name,
            feature.currentValue,
            feature.isRunning,
            feature.sourceType?.name
        )
        params.addString(feature.runningReferenceId)
        params.addUUID(feature.deviceId)
        this.pgPool.preparedQuery(query).execute(params).awaitSuspending()
        return this.getFeatureById(feature.id)!!
    }

    suspend fun updateFeature(feature: Feature): Feature {
        val query = """
            update features set 
                current_value = $1,
                feature_type = $2,
                is_running = $3,
                running_reference_id = $4,
                source_type = $5
            where id = $6
        """
        val params = Tuple.of(
            feature.currentValue,
            feature.featureType.name,
            feature.isRunning,
            feature.runningReferenceId,
            feature.sourceType?.name,
            feature.id
        )
        this.pgPool.preparedQuery(query).execute(params).awaitSuspending()
        return this.getFeatureById(feature.id)!!
    }

    suspend fun getDeviceFeatures(deviceId: UUID): List<Feature> {
        val query = """
           select *
            from features
            where device_id = $1
        """
        val params = Tuple.of(deviceId)
        return this.pgPool.preparedQuery(query).execute(params)
            .onItem()
            .transformToMulti { rows -> Multi.createFrom().iterable(rows) }
            .map { row -> Feature(row) }
            .collect().asList()
            .awaitSuspending()
    }

    suspend fun getFeatureByDeviceAndName(deviceId: UUID, name: String): Feature? {
        val query = """
           select *
            from features
            where device_id = $1 and name = $2
        """
        val params = Tuple.of(deviceId, name)
        val rows = this.pgPool.preparedQuery(query).execute(params).awaitSuspending()
        val rowIterator = rows.iterator()
        if (rowIterator.hasNext()) {
            return Feature(rowIterator.next())
        }
        return null
    }
}
