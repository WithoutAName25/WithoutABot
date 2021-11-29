package eu.withoutaname.discordbots.withoutabot.config

import net.dzikoysk.cdn.source.Resource
import panda.std.Result
import panda.utilities.FileUtils
import panda.utilities.StringUtils
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

class ConfigSource(private val path: Path, private val encoding: Charset) : Resource {
	
	constructor(path: Path) : this(path, StandardCharsets.UTF_8)
	
	override fun save(content: String?): Result<String?, IOException> {
		return Result.attempt(IOException::class.java) {
			FileUtils.overrideFile(path.toFile(), content)
			content
		}
	}
	
	override fun getSource(): String {
		return if (!Files.exists(path)) {
			StringUtils.EMPTY
		} else try {
			String(Files.readAllBytes(path), encoding)
		} catch (ioException: IOException) {
			throw IllegalStateException("Cannot read file", ioException)
		}
	}
}