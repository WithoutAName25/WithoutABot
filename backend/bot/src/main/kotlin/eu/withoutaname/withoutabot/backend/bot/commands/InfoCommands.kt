package eu.withoutaname.withoutabot.backend.bot.commands

import dev.kord.common.Color
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.message.create.InteractionResponseCreateBuilder
import dev.kord.rest.builder.message.create.actionRow
import dev.kord.rest.builder.message.create.embed
import eu.withoutaname.withoutabot.backend.bot.ConfigContext
import eu.withoutaname.withoutabot.backend.bot.commands.api.createChatInputCommand
import eu.withoutaname.withoutabot.backend.bot.dao.InfoCommandDefinition
import eu.withoutaname.withoutabot.backend.bot.dao.InfoCommandUsage
import eu.withoutaname.withoutabot.backend.common.LoggingContext
import eu.withoutaname.withoutabot.backend.common.tables.InfoCommands
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

context(ConfigContext, LoggingContext) suspend
fun Kord.infoCommands() {
    newSuspendedTransaction {
        InfoCommandDefinition.find { InfoCommands.enabled eq true }.forEach {
            createChatInputCommand(it.command, it.description)
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
    command.actionRows.forEach { actionRow ->
        if (actionRow.actions.isNotEmpty()) {
            actionRow {
                actionRow.actions.forEach { action ->
                    linkButton(action.url) {
                        label = action.label
                    }
                }
            }
        }
    }
}