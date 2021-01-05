package iot.tetracubered.data.repositories

import io.smallrye.mutiny.Uni
import io.vertx.mutiny.pgclient.PgPool
import io.vertx.mutiny.sqlclient.Tuple
import iot.tetracubered.data.entities.Action
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ActionRepository(private val pgPool: PgPool) {

    fun actionExistsByActionId(deviceId: UUID, actionId: UUID): Uni<Boolean> {
        val query = """
            select count(actions.id) > 0 as action_exists
            from actions
            inner join devices d on d.id = actions.device_id
            where d.circuit_id = $1 and actions.action_id = $2
        """
        val params = Tuple.of(deviceId, actionId)
        return this.pgPool.preparedQuery(query).execute(params)
            .map { rows ->
                val rowIterator = rows.iterator()
                if (rowIterator.hasNext()) {
                    rowIterator.next().getBoolean("action_exists")
                } else {
                    false
                }
            }
    }

    fun getActionByActionId(deviceId: UUID, actionId: UUID): Uni<Action> {
        val query = """
            select actions.*
            from actions
             inner join devices d on d.id = actions.device_id
            where d.circuit_id = $1 and actions.action_id = $2
        """
        val params = Tuple.of(deviceId, actionId)
        return this.pgPool.preparedQuery(query).execute(params)
            .map { rows -> Action(rows.iterator().next()) }
    }

    fun saveAction(action: Action): Uni<Action> {
        val query = """
            insert into actions (id, action_id, name, command_topic, device_id) 
            values ($1, $2, $3, $4, $5)
            returning *
        """
        val params = Tuple.of(action.id, action.actionId, action.name, action.commandTopic, action.deviceId)
        return this.pgPool.preparedQuery(query).execute(params)
            .map { rows -> Action(rows.iterator().next()) }
    }
}