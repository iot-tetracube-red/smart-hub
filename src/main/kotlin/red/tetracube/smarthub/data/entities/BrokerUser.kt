package red.tetracube.smarthub.data.entities

data class BrokerUser(
    val id: Long,
    val isSuperUser: Boolean,
    val clientId: String,
    val username: String,
    val password: String,
    val salt: String? = null
)
