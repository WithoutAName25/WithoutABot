package eu.withoutaname.discordbots.withoutabot.config

import net.dzikoysk.cdn.entity.Contextual
import net.dzikoysk.cdn.entity.Description
import panda.std.reactive.reference
import java.io.Serializable

class Config : Serializable {
	
	@Description("Settings for the discord bot")
	@JvmField
	val discord = reference(DiscordConfig())
	
	@Contextual
	class DiscordConfig : Serializable {
		
		@Description("The token to connect to the discord application")
		@JvmField
		var token = reference("")
	}
}