package iot.tetracubered.data.repositories

import io.smallrye.mutiny.Uni
import io.vertx.mutiny.pgclient.PgPool
import io.vertx.mutiny.sqlclient.Row
import io.vertx.mutiny.sqlclient.Tuple
import iot.tetracubered.data.entities.Action
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ActionRepository(private val pgPool: PgPool) {

    fun getActionById(actionId: UUID): Uni<Action?> {
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
    }

    fun storeAction(action: Action): Uni<Action> {
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
        return this.pgPool.preparedQuery(query).execute(params)
            .chain { _ -> this.getActionById(action.id) }
    }

    fun updateAction(action: Action): Uni<Action> {
        val query = """
           update actions set
            trigger_topic = $1
           where id = $2 
        """
        val params = Tuple.of(
            action.triggerTopic,
            action.id
        )
        return this.pgPool.preparedQuery(query).execute(params)
            .chain { _ -> this.getActionById(action.id) }
    }
}