package eu.withoutaname.discordbots.withoutabot

import eu.withoutaname.discordbots.withoutabot.config.Config
import eu.withoutaname.discordbots.withoutabot.config.ActivityConfig
import net.dzikoysk.cdn.Cdn
import net.dzikoysk.cdn.module.standard.StandardModule
import net.dzikoysk.cdn.source.Source
import kotlin.io.path.Path
import kotlin.io.path.exists

object WithoutABotFactory {
	
	fun createWithoutABot(): WithoutABot {
		val configPath = Path("withoutabot.cdn")
		val cdn = Cdn.configure()
			.registerModule(StandardModule())
			.withComposer(ActivityConfig.DEFAULT.javaClass, ActivityConfig.Serializer(), ActivityConfig.Deserializer())
			.build()
		val config = if (configPath.exists()) cdn.load(Source.of(configPath), Config()) else Config()
		cdn.render(config, configPath)
		
		return WithoutABot(
				config
		)
	}
}