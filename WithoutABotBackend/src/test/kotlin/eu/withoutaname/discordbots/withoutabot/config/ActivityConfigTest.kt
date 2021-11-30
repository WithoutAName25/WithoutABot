package eu.withoutaname.discordbots.withoutabot.config

import net.dv8tion.jda.api.entities.Activity
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ActivityConfigTest : WithAssertions {
	
	private val serializer = ActivityConfig.Serializer()
	private val deserializer = ActivityConfig.Deserializer()
	
	@ParameterizedTest
	@MethodSource("testSerializerParams")
	fun testSerializer(input: ActivityConfig?, expected: String?) {
		val result = serializer.serialize(input)
		assertThat(result.isOk).isTrue
		assertThat(result.get()).isEqualTo(expected)
	}
	
	private fun testSerializerParams(): Stream<Arguments> {
		return Stream.of(
				arguments(
						ActivityConfig(Activity.ActivityType.DEFAULT, "test status"),
						"DEFAULT test status"
				),
				arguments(
						ActivityConfig(Activity.ActivityType.WATCHING, "test status"),
						"WATCHING test status"
				)
		)
	}
	
	@ParameterizedTest
	@MethodSource("testDeserializerParams")
	fun testDeserializer(input: String?, expected: ActivityConfig?) {
		val result = deserializer.deserialize(input)
		assertThat(result.isOk).isTrue
		if (expected == null) {
			assertThat(result.get()).isNull()
		} else {
			assertThat(result.get().type).isEqualTo(expected.type)
			assertThat(result.get().name).isEqualTo(expected.name)
		}
	}
	
	private fun testDeserializerParams(): Stream<Arguments> {
		return Stream.of(
				arguments(
						"test status",
						ActivityConfig(Activity.ActivityType.DEFAULT, "test status")
				),
				arguments(
						"DEFAULT test status",
						ActivityConfig(Activity.ActivityType.DEFAULT, "test status")
				),
				arguments(
						"PLAYING test status",
						ActivityConfig(Activity.ActivityType.DEFAULT, "test status")
				),
				arguments(
						"WATCHING test status",
						ActivityConfig(Activity.ActivityType.WATCHING, "test status")
				),
				arguments(
						null,
						ActivityConfig.DEFAULT
				)
		)
	}
}