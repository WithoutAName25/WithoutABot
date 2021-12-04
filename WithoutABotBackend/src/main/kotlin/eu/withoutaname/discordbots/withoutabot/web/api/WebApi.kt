package eu.withoutaname.discordbots.withoutabot.web.api

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.util.concurrent.TimeUnit

object WebApi {
	
	private val server = embeddedServer(Netty, port = 8080) {
		routing {
			get("/") {
				call.respondText("Hi, das ist eine krasse api")
			}
		}
	}
	
	fun launch() {
		server.start(wait = false)
	}
	
	fun stop() {
		server.stop(20, 20, TimeUnit.SECONDS)
	}
}