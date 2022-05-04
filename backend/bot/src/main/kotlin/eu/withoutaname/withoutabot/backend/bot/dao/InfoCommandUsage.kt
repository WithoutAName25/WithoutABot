package eu.withoutaname.withoutabot.backend.bot.dao

import eu.withoutaname.withoutabot.backend.common.tables.InfoCommandActionRows
import eu.withoutaname.withoutabot.backend.common.tables.InfoCommands
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class InfoCommandUsage(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<InfoCommandUsage>(InfoCommands)

    val info by InfoCommands.info
    val ephemeral by InfoCommands.ephemeral
    val embed by InfoCommands.embed
    val color by InfoCommands.color
    private val unorderedActionRows by InfoCommandActionRowUsage referrersOn InfoCommandActionRows.command
    val actionRows by lazy { unorderedActionRows.sortedBy { it.position } }
}