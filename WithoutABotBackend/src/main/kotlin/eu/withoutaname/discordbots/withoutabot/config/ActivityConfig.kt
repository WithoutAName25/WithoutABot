package eu.withoutaname.discordbots.withoutabot.config

import net.dv8tion.jda.api.entities.Activity
import net.dzikoysk.cdn.serdes.SimpleDeserializer
import net.dzikoysk.cdn.serdes.SimpleSerializer
import panda.std.Result

class ActivityConfig(
		val type: Activity.ActivityType,
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
					val activity: Activity.ActivityType = try {
						val typeString = it.split(" ")[0]
						val type =
							if (typeString.equals("PLAYING", ignoreCase = true))
								Activity.ActivityType.DEFAULT
							else
								Activity.ActivityType.valueOf(typeString.uppercase())
						name = it.substring(typeString.length + 1)
						type
					} catch (e: IllegalArgumentException) {
						Activity.ActivityType.DEFAULT
					}
					ActivityConfig(activity, name)
				} ?: DEFAULT
			}
		}
		
	}
	
	companion object {
		
		val DEFAULT = ActivityConfig(Activity.ActivityType.DEFAULT, "nothing")
	}
}