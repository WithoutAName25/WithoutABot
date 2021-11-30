package eu.withoutaname.discordbots.withoutabot.bot.listener.command

import eu.withoutaname.discordbots.withoutabot.bot.getDeleteButton
import eu.withoutaname.discordbots.withoutabot.bot.getMessageEmbed
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object PingListener : ListenerAdapter() {
	
	override fun onSlashCommand(event: SlashCommandEvent) {
		if ("ping".equals(event.name, ignoreCase = true)) {
			event.replyEmbeds(getMessageEmbed("pong!"))
				.addActionRow(
						getDeleteButton(event.user)
				).queue()
		}
	}
}