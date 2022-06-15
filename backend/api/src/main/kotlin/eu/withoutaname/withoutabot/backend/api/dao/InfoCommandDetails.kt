package eu.withoutaname.withoutabot.backend.api.dao

import eu.withoutaname.withoutabot.backend.common.tables.InfoCommandActions
import eu.withoutaname.withoutabot.backend.common.tables.InfoCommands
import eu.withoutaname.withoutabot.common.api.InfoCommandAction
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class InfoCommandDetails(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<InfoCommandDetails>(InfoCommands)

    val server by InfoCommands.server
    val command by InfoCommands.command
    val description by InfoCommands.description
    val info by InfoCommands.info
    val ephemeral by InfoCommands.ephemeral
    val embed by InfoCommands.embed
    val color by InfoCommands.color
    val enabled by InfoCommands.enabled
    val actions by lazy {
        transaction {
            InfoCommandActions.select { InfoCommandActions.command eq id }.map {
                InfoCommandAction(
                    it[InfoCommandActions.row],
                    it[InfoCommandActions.position],
                    it[InfoCommandActions.url],
                    it[InfoCommandActions.label]
                )
            }.sortedBy { it.position }.groupBy { it.row }.values.toList()
        }
    }
}