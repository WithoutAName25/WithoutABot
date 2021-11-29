package eu.withoutaname.discordbots.withoutabot.bot.listener.command

import eu.withoutaname.discordbots.withoutabot.bot.HelperFunctions
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object PingListener : ListenerAdapter() {
	
	override fun onSlashCommand(event: SlashCommandEvent) {
		if ("ping".equals(event.name, ignoreCase = true)) {
			event.replyEmbeds(HelperFunctions.getMessageEmbed("pong!"))
				.addActionRow(
						HelperFunctions.getDeleteButton(event.user)
				).queue()
		}
	}
}