package eu.withoutaname.withoutabot.tables

import dev.kord.common.entity.ActivityType
import org.jetbrains.exposed.dao.id.IdTable

object Statuses : IdTable<Int>() {

    override val id = integer("id").entityId()
    val type = enumerationByName<ActivityType>("type", 9).default(ActivityType.Game)
    val name = varchar("name", 255)
    val url = varchar("url", 255).nullable()
    val duration = integer("duration").default(30)

    override val primaryKey = PrimaryKey(id)
}