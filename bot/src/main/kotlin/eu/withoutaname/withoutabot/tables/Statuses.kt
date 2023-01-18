package eu.withoutaname.withoutabot.tables

import eu.withoutaname.withoutabot.dao.ActivityTypeEnum
import org.jetbrains.exposed.dao.id.IdTable

object Statuses : IdTable<Int>() {

    override val id = integer("id").entityId()
    val type = enumeration<ActivityTypeEnum>("type").default(ActivityTypeEnum.PLAYING)
    val name = varchar("name", 255)
    val url = varchar("url", 255).nullable()
    val duration = integer("duration").default(30)

    override val primaryKey = PrimaryKey(id)
}
