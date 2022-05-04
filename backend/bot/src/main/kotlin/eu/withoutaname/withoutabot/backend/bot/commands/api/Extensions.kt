package eu.withoutaname.withoutabot.backend.bot.commands.api

import dev.kord.common.Color
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import dev.kord.rest.builder.message.create.embed
import eu.withoutaname.withoutabot.backend.bot.ConfigContext
import eu.withoutaname.withoutabot.backend.common.LoggingContext
import kotlin.coroutines.cancellation.CancellationException

context(ConfigContext, LoggingContext) suspend
fun Kord.commands(block: Commands.() -> Unit) {
    val commands = Commands().apply(block)
    with(commands) { register() }
    on<ChatInputCommandInteractionCreateEvent> {
        try {
            with(commands) { handle() }
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            interaction.respondEphemeral {
                embed {
                    description = "An error occurred while trying to handle your command!"
                    color = Color(255, 0, 0)
                }
            }
            log.warn("Error occurred while trying to handle command.", e)
        }
    }
}

context(ConfigContext) suspend
fun Kord.createChatInputCommand(
    name: String,
    description: String,
    builder: ChatInputCreateBuilder.() -> Unit = {}
) {
    val guildId = config.testServer
    if (guildId == null) {
        createGlobalChatInputCommand(name, description, builder)
    } else {
        createGuildChatInputCommand(guildId, name, description, builder)
    }
}