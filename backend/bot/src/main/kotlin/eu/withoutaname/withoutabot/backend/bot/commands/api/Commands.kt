package eu.withoutaname.withoutabot.backend.bot.commands.api

import dev.kord.core.Kord
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import eu.withoutaname.withoutabot.backend.bot.ConfigContext
import kotlin.reflect.KClass

class Commands {

    private val commands = mutableMapOf<String, Command<*>>()

    inline fun <reified T : Any> on(noinline block: suspend ChatInputCommandInteractionCreateEvent.(T) -> Unit) {
        on(T::class, block)
    }

    fun <T : Any> on(kClass: KClass<T>, block: suspend ChatInputCommandInteractionCreateEvent.(T) -> Unit) {
        val command = Command(kClass, block)
        commands[command.name] = command
    }

    context(ConfigContext) suspend fun Kord.register() {
        commands.values.forEach { with(it) { registerCommand() } }
    }

    suspend fun ChatInputCommandInteractionCreateEvent.handle() {
        commands[interaction.command.rootName]?.let {
            with(it) { handleCommand() }
        }
    }
}

