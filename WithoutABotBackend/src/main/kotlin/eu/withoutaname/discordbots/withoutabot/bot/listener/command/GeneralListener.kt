package eu.withoutaname.discordbots.withoutabot.bot.listener.command

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object GeneralListener : ListenerAdapter() {
	
	override fun onButtonClick(event: ButtonClickEvent) {
		if ("delete" == event.componentId || "delete_" + event.user.id == event.componentId) {
			event.message.delete().queue()
		}
	}
}