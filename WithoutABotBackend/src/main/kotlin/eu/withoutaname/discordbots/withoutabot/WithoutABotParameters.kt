package eu.withoutaname.discordbots.withoutabot

import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Command(name = "withoutabot")
class WithoutABotParameters : Runnable {
	
	@Option(names = ["--help", "-h"], usageHelp = true, description = ["Display help message"])
	var usageHelpRequested = false
	
	override fun run() {}
}