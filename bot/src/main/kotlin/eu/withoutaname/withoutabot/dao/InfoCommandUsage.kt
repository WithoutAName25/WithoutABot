package eu.withoutaname.withoutabot.dao

import eu.withoutaname.withoutabot.tables.InfoCommandActions
import eu.withoutaname.withoutabot.tables.InfoCommands
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class InfoCommandUsage(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<InfoCommandUsage>(InfoCommands)

    val info by InfoCommands.info
    val ephemeral by InfoCommands.ephemeral
    val embed by InfoCommands.embed
    val color by InfoCommands.color
    val actions by lazy {
        transaction {
            InfoCommandActions.select { InfoCommandActions.command eq id }.map {
                InfoCommandAction(
                    it[InfoCommandActions.row],
                    it[InfoCommandActions.position],
                    it[InfoCommandActions.url],
                    it[InfoCommandActions.label]
                )
            }.sortedBy { it.position }.groupBy { it.row }.values
        }
    }
}