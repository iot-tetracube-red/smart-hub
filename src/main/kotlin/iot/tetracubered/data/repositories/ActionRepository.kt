package iot.tetracubered.data.repositories

import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.mutiny.pgclient.PgPool
import io.vertx.mutiny.sqlclient.Row
import io.vertx.mutiny.sqlclient.Tuple
import iot.tetracubered.data.entities.Action
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ActionRepository(private val pgPool: PgPool) {

    suspend fun getActionById(actionId: UUID): Action? {
        val query = """
            select *
            from actions
            where id = $1
        """
        val params = Tuple.of(actionId)
        return this.pgPool.preparedQuery(query).execute(params)
            .map { rows -> rows.iterator() }
            .map { rowIterator -> if (rowIterator.hasNext()) rowIterator.next() else null }
            .map { row: Row? -> if (row != null) Action(row) else null }
            .awaitSuspending()
    }

    suspend fun storeAction(action: Action): Action {
        val query = """
            insert into actions(id, trigger_topic, name, feature_id) 
            values ($1, $2, $3, $4)
        """
        val params = Tuple.of(
            action.id,
            action.triggerTopic,
            action.name,
            action.featureId
        )
        this.pgPool.preparedQuery(query).execute(params).awaitSuspending()
        return this.getActionById(action.id)!!
    }

    suspend fun updateAction(action: Action): Action {
        val query = """
           update actions set
            trigger_topic = $1
           where id = $2 
        """
        val params = Tuple.of(
            action.triggerTopic,
            action.id
        )
        this.pgPool.preparedQuery(query).execute(params)
        return this.getActionById(action.id)!!
    }
}