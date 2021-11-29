package eu.withoutaname.discordbots.withoutabot

import eu.withoutaname.discordbots.withoutabot.bot.Bot
import eu.withoutaname.discordbots.withoutabot.config.Config
import java.util.concurrent.atomic.AtomicBoolean

class WithoutABot(
		private val config: Config
) {
	
	private val alive = AtomicBoolean(false)
	private val shutdownHook = Thread {
		shutdown()
	}
	
	fun start() {
		try {
			alive.set(true)
			Bot.start(config.bot.get())
			Runtime.getRuntime().addShutdownHook(shutdownHook)
		} catch (e: Exception) {
			e.printStackTrace()
			shutdown()
		}
	}
	
	private fun shutdown() {
		if (alive.get()) {
			Bot.shutdown()
			alive.set(false)
		}
	}
}