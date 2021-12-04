package eu.withoutaname.discordbots.withoutabot

import eu.withoutaname.discordbots.withoutabot.bot.Bot
import eu.withoutaname.discordbots.withoutabot.config.Config
import eu.withoutaname.discordbots.withoutabot.web.api.WebApi
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicBoolean

class WithoutABot(
		private val config: Config
) {
	
	private val logger = LoggerFactory.getLogger(this::class.java)
	
	private val alive = AtomicBoolean(false)
	private val shutdownHook = Thread {
		shutdown()
	}
	
	fun start() {
		try {
			logger.info("Starting...")
			alive.set(true)
			Bot.start(config.bot.get())
			WebApi.launch()
			Runtime.getRuntime().addShutdownHook(shutdownHook)
			logger.info("Started.")
		} catch (e: Exception) {
			e.printStackTrace()
			shutdown()
		}
	}
	
	private fun shutdown() {
		if (alive.get()) {
			logger.info("Stopping...")
			Bot.shutdown()
			WebApi.stop()
			alive.set(false)
			logger.info("Stopped.")
		}
	}
}