package eu.withoutaname.withoutabot.backend.common.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object InfoCommandActionRows : IntIdTable() {

    val command = reference("command", InfoCommands)
    var position = byte("position").check { it.between(0, 4) }
}