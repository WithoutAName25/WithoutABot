package eu.withoutaname.discordbots.withoutabot

import picocli.CommandLine
import java.io.OutputStream
import java.io.PrintStream

object WithoutABotLauncher {
	
	fun create(vararg args: String, printStream: PrintStream? = System.out): WithoutABot? {
		val parameters = WithoutABotParameters()
		val cmdLine = CommandLine(parameters)
		cmdLine.execute(*args)
		
		if (parameters.usageHelpRequested) {
			val stream: PrintStream = (printStream ?: PrintStream(object : OutputStream() {
				override fun write(p0: Int) {}
			}))
			cmdLine.usage(stream)
			return null
		}
		
		return WithoutABotFactory.createWithoutABot()
	}
}

fun main(args: Array<String>) {
	WithoutABotLauncher.create(*args)?.launch()
}