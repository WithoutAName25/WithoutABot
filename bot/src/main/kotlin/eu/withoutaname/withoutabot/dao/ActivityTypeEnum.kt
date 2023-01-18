package eu.withoutaname.withoutabot.dao

import dev.kord.gateway.builder.PresenceBuilder

enum class ActivityTypeEnum(val apply: PresenceBuilder.(Status) -> Unit) {
    PLAYING({ playing(it.name) }),
    LISTENING({ listening(it.name) }),
    WATCHING({ watching(it.name) }),
    COMPETING({ competing(it.name) }),
    STREAMING({ streaming(it.name, it.url!!) })
}
