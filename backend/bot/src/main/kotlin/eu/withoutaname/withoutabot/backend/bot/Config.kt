package eu.withoutaname.withoutabot.backend.bot

import dev.kord.common.entity.Snowflake

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