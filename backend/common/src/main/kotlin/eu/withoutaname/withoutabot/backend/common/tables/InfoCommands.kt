package eu.withoutaname.withoutabot.backend.common.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object InfoCommands : IntIdTable() {

    val command = varchar("command", 50).uniqueIndex()
    val description = varchar("description", 255)
    val info = varchar("value", 10000)
    val ephemeral = bool("ephemeral").default(true)
    val embed = bool("embed").default(true)
    val color = integer("color").nullable()
    val enabled = bool("enabled").default(true)
}