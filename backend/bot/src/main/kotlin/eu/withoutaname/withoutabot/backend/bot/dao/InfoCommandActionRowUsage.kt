package eu.withoutaname.withoutabot.backend.bot.dao

import eu.withoutaname.withoutabot.backend.common.tables.InfoCommandActionRows
import eu.withoutaname.withoutabot.backend.common.tables.InfoCommandActions
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class InfoCommandActionRowUsage(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<InfoCommandActionRowUsage>(InfoCommandActionRows)

    val position by InfoCommandActionRows.position
    private val unorderedActions by InfoCommandAction referrersOn InfoCommandActions.row
    val actions by lazy { unorderedActions.sortedBy { it.position } }
}