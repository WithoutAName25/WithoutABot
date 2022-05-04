package eu.withoutaname.withoutabot.backend.bot

import dev.kord.core.Kord
import eu.withoutaname.withoutabot.backend.bot.commands.api.commands
import eu.withoutaname.withoutabot.backend.bot.commands.infoCommands
import eu.withoutaname.withoutabot.backend.bot.commands.testCommand
import eu.withoutaname.withoutabot.backend.common.context
import eu.withoutaname.withoutabot.backend.common.initDB
import org.slf4j.LoggerFactory

suspend fun main() {
    with(LoggerFactory.getLogger("WithoutABot").context()) {
        initDB()
        with(loadConfig().context()) {
            Kord(config.token) {
                this.enableShutdownHook = true
            }.apply {
                infoCommands()
                commands {
                    testCommand()
                }
            }.login()
        }
    }
}