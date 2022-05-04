package eu.withoutaname.withoutabot.backend.bot.dao

import eu.withoutaname.withoutabot.backend.common.tables.InfoCommandActions
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class InfoCommandAction(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<InfoCommandAction>(InfoCommandActions)

    val position by InfoCommandActions.position
    val url by InfoCommandActions.url
    val label by InfoCommandActions.label
}