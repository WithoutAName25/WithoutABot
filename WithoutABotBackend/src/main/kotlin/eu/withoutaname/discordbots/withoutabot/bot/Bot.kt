package eu.withoutaname.discordbots.withoutabot.bot

import eu.withoutaname.discordbots.withoutabot.bot.listener.command.GeneralListener
import eu.withoutaname.discordbots.withoutabot.bot.listener.command.PingListener
import eu.withoutaname.discordbots.withoutabot.config.ActivityConfig
import eu.withoutaname.discordbots.withoutabot.config.Config
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import java.util.*

object Bot {
	
	lateinit var jda: JDA
		private set
	private lateinit var activityTimer: Timer
	
	fun start(config: Config.BotConfig) {
		jda =
			JDABuilder
				.createDefault(config.token.get())
				.addEventListeners(
						GeneralListener,
						PingListener
				)
				.build()
		setupActivities(config)
		jda.awaitReady()
		jda.updateCommands()
			.addCommands(
					CommandData("info", "Shows all commands and a link to this page"),
					CommandData("mods", "Shows all mods from me (WithoutAName)"),
					CommandData("ping", "Try it out!"),
					CommandData(
							"roleselection",
							"[Admin] Adds an emote to a message where everyone gets a specific role by clicking on the emote"
					)
						.addOption(OptionType.INTEGER, "messageid", "MessageId to add role selection", true)
			).queue()
		println("Bot online")
	}
	
	fun shutdown() {
		if (this::jda.isInitialized) jda.shutdown()
		if (this::activityTimer.isInitialized) activityTimer.cancel()
		println("Bot offline")
	}
	
	private fun setupActivities(config: Config.BotConfig) {
		fun setActivity(activity: ActivityConfig) {
			jda.presence.activity = Activity.of(activity.type, activity.name)
		}
		
		val activities = config.statusMessages.get()
		if (activities.size == 1) {
			setActivity(activities[0])
		} else if (activities.size > 1) {
			activityTimer = Timer()
			activityTimer.scheduleAtFixedRate(object : TimerTask() {
				private var i = 0
				override fun run() {
					if (i >= activities.size) {
						i = 0
					}
					setActivity(activities[i++])
				}
			}, 0, config.statusMessagesDelay.get())
		}
	}
	
}