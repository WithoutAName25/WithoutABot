package eu.withoutaname.withoutabot.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object InfoCommands : IntIdTable() {

    val server = long("server").nullable()
    val command = varchar("command", 50)
    val description = varchar("description", 255)
    val info = varchar("info", 10000)
    val ephemeral = bool("ephemeral").default(true)
    val embed = bool("embed").default(true)
    val color = integer("color").nullable()
    val enabled = bool("enabled").default(true)

    init {
        uniqueIndex(server, command)
    }
}