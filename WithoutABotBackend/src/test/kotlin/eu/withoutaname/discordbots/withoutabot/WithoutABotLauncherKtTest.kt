package eu.withoutaname.discordbots.withoutabot

import org.assertj.core.api.WithAssertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class WithoutABotLauncherKtTest : WithAssertions {
	
	@ParameterizedTest
	@ValueSource(strings = ["--help", "-h"])
	fun launchHelp(arg: String) {
		assertThat(WithoutABotLauncher.create(arg, printStream = null))
			.isNull()
	}
}