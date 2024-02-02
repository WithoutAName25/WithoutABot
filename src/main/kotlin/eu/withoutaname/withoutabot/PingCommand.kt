package eu.withoutaname.withoutabot

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand

class PingCommand : Extension() {
    override val name = "ping"

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = "ping"
            description = "Is it online?"

            action {
                respond {
                    content = "Pong"
                }
            }
        }
    }
}
