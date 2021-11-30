package eu.withoutaname.discordbots.withoutabot.bot.listener.command

import eu.withoutaname.discordbots.withoutabot.bot.listener.mockUser
import eu.withoutaname.discordbots.withoutabot.bot.listener.onEvent
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.util.stream.Stream

internal class GeneralListenerTest {
	
	@ParameterizedTest
	@MethodSource("testOnButtonClickParams")
	fun testOnButtonClick(compId: String, userID: String, shouldDelete: Boolean) {
		val usr = mockUser(userID)
		val restAction = mock<AuditableRestAction<Void>>()
		val msg = mock<Message> {
			on { delete() } doReturn restAction
		}
		val event = mock<ButtonClickEvent> {
			on { componentId } doReturn compId
			on { user } doReturn usr
			on { message } doReturn msg
		}
		
		onEvent(event)
		
		if (shouldDelete) {
			Mockito.verify(restAction).queue()
		} else {
			Mockito.verify(msg, Mockito.never()).delete()
			Mockito.verify(restAction, Mockito.never()).queue()
		}
	}
	
	companion object {
		
		@JvmStatic
		fun testOnButtonClickParams(): Stream<Arguments> {
			return Stream.of(
					arguments("delete", "anyUserId", true),
					arguments("delete_userId", "userId", true),
					arguments("delete_userId", "otherUserId", false),
					arguments("anyComponentId", "anyUserId", false)
			)
		}
	}
	
}