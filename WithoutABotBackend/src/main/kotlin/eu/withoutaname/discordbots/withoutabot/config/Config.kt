package eu.withoutaname.discordbots.withoutabot.config

import net.dzikoysk.cdn.entity.Contextual
import net.dzikoysk.cdn.entity.Description
import panda.std.reactive.mutableReference
import java.io.Serializable

class Config : Serializable {
	
	@JvmField
	@Description("# Settings for the discord bot")
	val bot = mutableReference(BotConfig())
	
	@Contextual
	class BotConfig : Serializable {
		
		@JvmField
		@Description("# The token to connect to the discord application")
		val token = mutableReference("")
		
		@JvmField
		@Description("")
		@Description("# Time to wait before showing the next status message (time in ms)")
		val statusMessagesDelay = mutableReference(5000L)
		
		@JvmField
		@Description("# List of messages for status messages")
		@Description("# Use DEFAULT, LISTENING, WATCHING or COMPETING as prefix")
		val statusMessages = listOf(ActivityConfig.DEFAULT)
	}
}