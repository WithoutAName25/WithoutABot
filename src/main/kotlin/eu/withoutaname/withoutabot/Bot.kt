package eu.withoutaname.withoutabot

import dev.kordex.core.ExtensibleBot
import dev.kordex.core.utils.env
import dev.kordex.core.utils.envOrNull

suspend fun main() {
    val bot = ExtensibleBot(env("BOT_TOKEN")) {
        applicationCommands {
            envOrNull("TEST_GUILD_ID")?.let { defaultGuild(it) }
        }
        extensions {
            add(::PingCommand)
        }
    }

    bot.start()
}
