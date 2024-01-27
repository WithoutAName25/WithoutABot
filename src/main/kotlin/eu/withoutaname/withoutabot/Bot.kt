package eu.withoutaname.withoutabot

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import eu.withoutaname.withoutabot.interactions.interactions
import kotlinx.coroutines.flow.forEach

suspend fun main() {
    val kord = Kord(
        System.getenv("BOT_TOKEN")
            ?: throw IllegalStateException("Environment variable BOT_TOKEN is not set!")
    )

    kord.interactions {
        command("hello", Snowflake(824744192202768394)) {
            description = "It could be a simple hello world..."

            val nameArg = string("name") {
                description = "What is your name?"
                required = false
            }

            action {
                val name = nameArg.getValue() ?: "world"
                interaction.respondEphemeral {
                    content = "Hello, $name!"
                }
            }
        }
    }


    kord.login()
}
