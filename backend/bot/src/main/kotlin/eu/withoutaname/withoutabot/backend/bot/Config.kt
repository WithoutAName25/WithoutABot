package eu.withoutaname.withoutabot.backend.bot

import dev.kord.common.entity.Snowflake
import eu.withoutaname.withoutabot.backend.common.LoggingContext
import kotlin.system.exitProcess

data class Config(val token: String, val testServer: Snowflake? = null) {

    fun context(): ConfigContext {
        return object : ConfigContext {
            override val config = this@Config
        }
    }
}

interface ConfigContext {

    val config: Config
}

context(LoggingContext)
fun loadConfig(): Config {
    val token = System.getenv("BOT_TOKEN")

    if (token == null) {
        log.error("Missing environment variable 'BOT_TOKEN'!")
        exitProcess(-1)
    }
    return Config(token, System.getenv("TEST_SERVER")?.let { Snowflake(it) })
}