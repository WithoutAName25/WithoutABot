package eu.withoutaname.discordbots.withoutabot

import picocli.CommandLine

object WithoutABotLauncher {
	
	fun create(vararg args: String): WithoutABot? {
		val parameters = WithoutABotParameters()
		val cmdLine = CommandLine(parameters)
		cmdLine.execute(*args)
		
		return if (parameters.usageHelpRequested) null
		else WithoutABotFactory.createWithoutABot(parameters)
	}
}

fun main(args: Array<String>) {
	WithoutABotLauncher.create(*args)?.start()
}