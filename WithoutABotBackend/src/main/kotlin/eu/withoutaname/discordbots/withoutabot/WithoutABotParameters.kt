package eu.withoutaname.discordbots.withoutabot

import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.nio.file.Path
import kotlin.io.path.Path

@Command(name = "withoutabot")
class WithoutABotParameters : Runnable {
	
	@Option(names = ["--help", "-h"], usageHelp = true, description = ["Display help message"])
	var usageHelpRequested = false
	
	@Option(
			names = ["--config", "-cfg"],
			defaultValue = "withoutabot.cdn",
			showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
			description = ["Defines the location of the config file"]
	)
	lateinit var config: String
	lateinit var configPath: Path
	
	override fun run() {
		configPath = Path(config)
	}
}