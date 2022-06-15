package eu.withoutaname.withoutabot.backend.bot.dao

import eu.withoutaname.withoutabot.backend.common.tables.InfoCommands
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class InfoCommandDefinition(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<InfoCommandDefinition>(InfoCommands)

    val server by InfoCommands.server
    val command by InfoCommands.command
    val description by InfoCommands.description
}