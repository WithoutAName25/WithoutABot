package eu.withoutaname.discordbots.withoutabot

import eu.withoutaname.discordbots.withoutabot.config.Config
import net.dzikoysk.cdn.CdnFactory
import net.dzikoysk.cdn.source.Source
import kotlin.io.path.Path
import kotlin.io.path.exists

object WithoutABotFactory {
	
	fun createWithoutABot(): WithoutABot {
		val configPath = Path("withoutabot.cdn")
		val cdn = CdnFactory.createStandard()
		val config = if (configPath.exists()) cdn.load(Source.of(configPath), Config()) else Config()
		cdn.render(config, configPath)
		
		return WithoutABot(
				config
		)
	}
}