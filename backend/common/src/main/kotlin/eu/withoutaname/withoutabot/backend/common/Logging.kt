package eu.withoutaname.withoutabot.backend.common

import org.slf4j.Logger

interface LoggingContext {

    val log: Logger
}

fun Logger.context(): LoggingContext {
    return object : LoggingContext {
        override val log = this@context
    }
}