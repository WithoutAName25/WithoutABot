package eu.withoutaname.withoutabot.backend.common.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object InfoCommandActions : IntIdTable() {

    val row = reference("row", InfoCommandActionRows)
    val position = byte("position").check { it.between(0, 4) }
    val url = varchar("url", 255)
    val label = varchar("label", 255)
}