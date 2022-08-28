package eu.withoutaname.withoutabot.dao

import eu.withoutaname.withoutabot.tables.Statuses
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Status(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Status>(Statuses)

    val type by Statuses.type
    val name by Statuses.name
    val url by Statuses.url
    val duration by Statuses.duration
}
