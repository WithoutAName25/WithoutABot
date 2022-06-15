package eu.withoutaname.withoutabot.common.api

import kotlinx.serialization.Serializable

@Serializable
data class InfoCommand(
    var command: String,
    var description: String,
    var info: String,
    var ephemeral: Boolean = true,
    var embed: Boolean = true,
    var color: Int? = 0x0000FF,
    var enabled: Boolean = true,
    var actions: List<List<InfoCommandAction>>
)

@Serializable
data class InfoCommandAction(
    var row: Byte,
    var position: Byte,
    var url: String,
    var label: String
)

