package eu.withoutaname.withoutabot

import dev.kord.core.Kord
import eu.withoutaname.withoutabot.commands.api.commands
import eu.withoutaname.withoutabot.commands.countCommand
import eu.withoutaname.withoutabot.commands.infoCommands
import eu.withoutaname.withoutabot.dao.Status
import eu.withoutaname.withoutabot.tables.Statuses
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.seconds

suspend fun main() {
    with(LoggerFactory.getLogger("WithoutABot").context()) {
        initDB()
        with(loadConfig().context()) {
            Kord(config.token) {
                this.enableShutdownHook = true
            }.apply {
                infoCommands()
                commands {
                    countCommand()
                }
                configurePresence()
            }.login()
        }
    }
}

context(LoggingContext)
private fun Kord.configurePresence() {
    val job = launch {
        delay(5.seconds)
        while (true) {
            newSuspendedTransaction {
                val statuses = Status.all().orderBy(Statuses.id to SortOrder.ASC)
                if (statuses.empty()) {
                    editPresence {}
                    delay(30.seconds)
                    return@newSuspendedTransaction
                }
                statuses.forEach {
                    editPresence {
                        it.type.apply(this, it)
                    }
                    delay(it.duration.seconds)
                }
            }
        }
    }
    Runtime.getRuntime().addShutdownHook(Thread {
        job.cancel()
    })
}
