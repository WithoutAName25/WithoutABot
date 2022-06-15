package eu.withoutaname.withoutabot.backend.common.tables

import org.jetbrains.exposed.sql.Table

object InfoCommandActions : Table() {

    val command = reference("command", InfoCommands)
    val row = byte("row").check { it.between(0, 4) }
    val position = byte("position").check { it.between(0, 4) }
    val url = varchar("url", 255)
    val label = varchar("label", 255)

    init {
        uniqueIndex(command, row, position)
    }
}