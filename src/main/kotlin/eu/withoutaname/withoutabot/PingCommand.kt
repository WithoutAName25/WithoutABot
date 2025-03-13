package eu.withoutaname.withoutabot

import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.ephemeralSlashCommand
import dev.kordex.core.i18n.withContext
import eu.withoutaname.withoutabot.i18n.Translations

class PingCommand : Extension() {
    override val name = "ping"

    override suspend fun setup() {
        ephemeralSlashCommand {
            name = Translations.Command.Ping.name
            description = Translations.Command.Ping.description

            action {
                respond {
                    content = Translations.Command.Ping.description
                        .withContext(this@action)
                        .translate()
                }
            }
        }
    }
}
