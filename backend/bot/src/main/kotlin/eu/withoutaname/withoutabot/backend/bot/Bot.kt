package eu.withoutaname.withoutabot.backend.bot

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.rest.builder.message.create.embed
import eu.withoutaname.withoutabot.backend.bot.commands.Test
import eu.withoutaname.withoutabot.backend.bot.commands.api.commands
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
                    on<Test> {
                        interaction.respondEphemeral {
                            embed {
                                if (it.number <= 0) {
                                    description = "Number must be greater than 0"
                                } else if (it.number > 10) {
                                    description = "${it.number} is to big. Number should be smaller or equal than 10"
                                } else {
                                    description = ""
                                    for (i in 1..it.number) {
                                        if (i != 1) description += "; "
                                        description += i
                                    }
                                }
                            }
                        }
                    }
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