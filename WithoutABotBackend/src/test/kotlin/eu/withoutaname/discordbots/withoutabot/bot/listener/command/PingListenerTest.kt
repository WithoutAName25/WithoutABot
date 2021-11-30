package eu.withoutaname.discordbots.withoutabot.bot.listener.command

import eu.withoutaname.discordbots.withoutabot.bot.getDeleteButton
import eu.withoutaname.discordbots.withoutabot.bot.getMessageEmbed
import eu.withoutaname.discordbots.withoutabot.bot.listener.mockUser
import eu.withoutaname.discordbots.withoutabot.bot.listener.onEvent
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.components.Component
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

internal class PingListenerTest {
	
	@Test
	fun onSlashCommand() {
		val replyAction = mock<ReplyAction> {
			on { addActionRow(any() as Component) } doReturn mock
		}
		val mockUser = mockUser("anyUserId")
		val event = mock<SlashCommandEvent> {
			on { name } doReturn "PiNg"
			on { user } doReturn mockUser
			on { replyEmbeds(any() as MessageEmbed) } doReturn replyAction
		}
		
		onEvent(event)
		
		verify(event).replyEmbeds(getMessageEmbed("pong!"))
		verify(replyAction).addActionRow(getDeleteButton(mockUser))
		verify(replyAction).queue()
	}
	
}