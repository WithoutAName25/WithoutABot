package eu.withoutaname.discordbots.withoutabot.config

import dev.kord.common.entity.ActivityType
import net.dzikoysk.cdn.serdes.SimpleDeserializer
import net.dzikoysk.cdn.serdes.SimpleSerializer
import panda.std.Result

class ActivityConfig(
	val type: ActivityType,
	val name: String
) {
	
	class Serializer : SimpleSerializer<ActivityConfig> {
		
		override fun serialize(entity: ActivityConfig?): Result<String, Exception> {
			return Result.attempt {
				entity?.run { "${type.name} ${name.trim()}" } ?: "DEFAULT nothing"
			}
		}
		
	}
	
	class Deserializer : SimpleDeserializer<ActivityConfig> {
		
		override fun deserialize(source: String?): Result<ActivityConfig, Exception> {
			return Result.attempt {
				source?.trim()?.let {
					var name = it
					val activity: ActivityType = try {
						val typeString = it.split(" ")[0]
						val type =
							if (typeString.equals("PLAYING", ignoreCase = true))
								ActivityType.Game
							else
								ActivityType.valueOf(typeString.uppercase())
						name = it.substring(typeString.length + 1)
						type
					} catch (e: IllegalArgumentException) {
						ActivityType.Game
					}
					ActivityConfig(activity, name)
				} ?: DEFAULT
			}
		}
		
	}
	
	companion object {
		
		val DEFAULT = ActivityConfig(ActivityType.Game, "nothing")
	}
}