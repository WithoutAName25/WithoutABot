package eu.withoutaname.discordbots.withoutabot

import eu.withoutaname.discordbots.withoutabot.config.ActivityConfig
import eu.withoutaname.discordbots.withoutabot.config.Config
import eu.withoutaname.discordbots.withoutabot.config.ConfigSource
import net.dzikoysk.cdn.Cdn
import net.dzikoysk.cdn.module.standard.StandardModule

object WithoutABotFactory {
	
	fun createWithoutABot(parameters: WithoutABotParameters): WithoutABot {
		val cdn = Cdn.configure()
			.registerModule(StandardModule())
			.withComposer(ActivityConfig.DEFAULT.javaClass, ActivityConfig.Serializer(), ActivityConfig.Deserializer())
			.build()
		var config = Config()
		val configLoadResult = cdn.load(ConfigSource(parameters.configPath), config)
		if (configLoadResult.isOk) config = configLoadResult.get()
		
		return WithoutABot(
				config
		)
	}
}