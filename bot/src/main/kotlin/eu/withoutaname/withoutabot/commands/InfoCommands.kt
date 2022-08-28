package eu.withoutaname.withoutabot.commands

import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.message.create.InteractionResponseCreateBuilder
import dev.kord.rest.builder.message.create.actionRow
import dev.kord.rest.builder.message.create.embed
import eu.withoutaname.withoutabot.ConfigContext
import eu.withoutaname.withoutabot.LoggingContext
import eu.withoutaname.withoutabot.commands.api.createChatInputCommand
import eu.withoutaname.withoutabot.dao.InfoCommandDefinition
import eu.withoutaname.withoutabot.dao.InfoCommandUsage
import eu.withoutaname.withoutabot.tables.InfoCommands
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

context(ConfigContext, LoggingContext) suspend
fun Kord.infoCommands() {
    newSuspendedTransaction {
        InfoCommandDefinition.find { InfoCommands.enabled eq true }.forEach {
            val serverId = it.server
            if (serverId == null) {
                createChatInputCommand(it.command, it.description)
            } else {
                createGuildChatInputCommand(Snowflake(serverId), it.command, it.description)
            }
        }
    }

    on<ChatInputCommandInteractionCreateEvent> {
        newSuspendedTransaction {
            InfoCommandUsage.find { (InfoCommands.command eq interaction.command.rootName) and (InfoCommands.enabled eq true) }
                .firstOrNull()
                ?.let { command ->
                    if (command.ephemeral)
                        interaction.respondEphemeral { respond(command) }
                    else
                        interaction.respondPublic { respond(command) }
                }
        }
    }
}

private fun InteractionResponseCreateBuilder.respond(command: InfoCommandUsage) {
    if (command.embed) {
        embed {
            description = command.info
            command.color?.let {
                color = Color(it)
            }
        }
    } else {
        content = command.info
    }
    command.actions.forEach { actionRow ->
        actionRow {
            actionRow.forEach { action ->
                linkButton(action.url) {
                    label = action.label
                }
            }
        }
    }
}