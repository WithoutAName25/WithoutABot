package eu.withoutaname.discordbots.withoutabot.bot.listener

import eu.withoutaname.discordbots.withoutabot.bot.Bot
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.Event
import org.mockito.kotlin.KStubbing
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

fun onEvent(event: Event) {
	Bot.listeners.forEach { it.onEvent(event) }
}

fun mockUser(userId: String = "anyUserId", stubbing: KStubbing<User>.(User) -> Unit = {}): User {
	return mock {
		on { id } doReturn userId
		stubbing(mock)
	}
}