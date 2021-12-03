package eu.withoutaname.discordbots.withoutabot

import eu.withoutaname.discordbots.withoutabot.config.ActivityConfig
import eu.withoutaname.discordbots.withoutabot.config.Config
import eu.withoutaname.discordbots.withoutabot.config.ConfigSource
import net.dzikoysk.cdn.Cdn
import net.dzikoysk.cdn.module.json.JsonLikeModule
import net.dzikoysk.cdn.module.standard.StandardModule
import net.dzikoysk.cdn.module.yaml.YamlLikeModule
import kotlin.io.path.name

object WithoutABotFactory {
	
	fun createWithoutABot(parameters: WithoutABotParameters): WithoutABot {
		val cdn = Cdn.configure()
			.registerModule(
					when (parameters.configPath.fileName.name.split(".").last()) {
						"yml", "yaml" -> YamlLikeModule()
						"json" -> JsonLikeModule()
						else -> StandardModule()
					}
			)
			.withComposer(ActivityConfig.DEFAULT.javaClass, ActivityConfig.Serializer(), ActivityConfig.Deserializer())
			.build()
		var config = Config()
		val source = ConfigSource(parameters.configPath)
		val configLoadResult = cdn.load(source, config)
		if (configLoadResult.isOk) config = configLoadResult.get()
		cdn.render(config, source)
		
		return WithoutABot(
				config
		)
	}
}