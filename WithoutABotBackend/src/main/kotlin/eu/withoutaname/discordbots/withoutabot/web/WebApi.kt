package eu.withoutaname.discordbots.withoutabot.web

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.util.concurrent.TimeUnit

object WebApi {
	
	private val server = embeddedServer(Netty, port = 8080) {
		install(ContentNegotiation) {
			json()
		}
		install(Locations)
		install(StatusPages) {
			exception<NotImplementedError> { call.respond(HttpStatusCode.NotImplemented) }
		}
		routing {
		}
	}
	
	@JvmStatic
	fun main(args: Array<String>) {
		launch()
	}
	
	fun launch() {
		server.start(wait = false)
	}
	
	fun stop() {
		server.stop(20, 20, TimeUnit.SECONDS)
	}
}