package eu.withoutaname.withoutabot

import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.utils.env
import com.kotlindiscord.kord.extensions.utils.envOrNull

suspend fun main() {
    val bot = ExtensibleBot(env("BOT_TOKEN")) {
        applicationCommands {
            envOrNull("TEST_GUILD_ID")?.let { defaultGuild(it) }
        }
        extensions {
            add(::PingCommand)
            add(::TicTacToe)
        }
    }

    bot.start()
}
