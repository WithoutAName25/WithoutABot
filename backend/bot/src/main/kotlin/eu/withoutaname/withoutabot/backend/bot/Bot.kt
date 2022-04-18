package eu.withoutaname.withoutabot.backend.bot

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import eu.withoutaname.withoutabot.backend.bot.commands.api.commands
import eu.withoutaname.withoutabot.backend.bot.commands.testCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

interface LoggingContext {

    val log: Logger
}

fun Logger.context(): LoggingContext {
    return object : LoggingContext {
        override val log = this@context
    }
}

suspend fun main() {
    with(LoggerFactory.getLogger("WithoutABot").context()) {
        with(loadConfig().context()) {
            Kord(config.token) {
                this.enableShutdownHook = true
            }.apply {
                commands {
                    testCommand()
                }
            }.login()
        }
    }
}

context(LoggingContext) private fun loadConfig(): Config {
    val token = System.getenv("BOT_TOKEN")

    if (token == null) {
        log.error("Missing environment variable 'BOT_TOKEN'!")
        exitProcess(-1)
    }
    return Config(token, System.getenv("TEST_SERVER")?.let { Snowflake(it) })
}