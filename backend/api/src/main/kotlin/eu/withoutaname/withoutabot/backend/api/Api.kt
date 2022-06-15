package eu.withoutaname.withoutabot.backend.api

import dev.kord.rest.service.RestClient
import eu.withoutaname.withoutabot.backend.api.dao.InfoCommandDetails
import eu.withoutaname.withoutabot.backend.common.context
import eu.withoutaname.withoutabot.backend.common.initDB
import eu.withoutaname.withoutabot.backend.common.tables.InfoCommands
import eu.withoutaname.withoutabot.common.api.InfoCommand
import eu.withoutaname.withoutabot.common.api.routes.InfoCommandsRoute
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.hours
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation.Plugin as ClientContentNegotiation
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation

suspend fun main() {
    with(LoggerFactory.getLogger("WithoutABot").context()) {
        initDB()
        val applicationHttpClient = HttpClient(CIO) {
            install(ClientContentNegotiation) {
                json()
            }
        }
        embeddedServer(Netty) {
            install(Sessions) {
                cookie<UserSession>("user_session") {
                    cookie.path = "/"
                    cookie.maxAge = 24.hours
                }
            }
            install(Authentication) {
                session<UserSession>("auth-session") {
                    challenge {
                        call.respondRedirect("/login")
                    }
                    validate { it }
                }
                oauth("auth-oauth-discord") {
                    urlProvider = { "http://localhost:8888/oauth/callback" }
                    providerLookup = {
                        OAuthServerSettings.OAuth2ServerSettings(
                            "discord",
                            "https://discord.com/api/v10/oauth2/authorize",
                            "https://discord.com/api/v10/oauth2/token",
                            HttpMethod.Post,
                            System.getenv("DISCORD_CLIENT_ID"),
                            System.getenv("DISCORD_CLIENT_SECRET"),
                            listOf("identify", "guilds")
                        )
                    }
                    client = applicationHttpClient
                }
            }
            install(ServerContentNegotiation) {
                json()
            }
            install(Resources)
            install(CallLogging)
            routing {
                authenticate("auth-oauth-discord") {
                    get("/login") { }
                    get("/oauth/callback") {
                        val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
                        call.sessions.set(UserSession(principal?.accessToken.toString()))
                        call.respondRedirect("/hello")
                    }
                }
                authenticate("auth-session") {
                    get("/hello") {
                        call.principal<UserSession>()?.let {
//                            val response = applicationHttpClient.get("https://discord.com/api/v10/users/@me") {
//                                headers.append("Authorization", "Bearer ${it.token}")
//                            }
//                            call.respondText("Hello ${response.bodyAsText()}")

                            val restClient = RestClient(it.token)
                            call.respond(restClient.user.getCurrentUser())
                        }
                    }
                }

                get<InfoCommandsRoute.Names> {
                    newSuspendedTransaction {
                        val response =
                            InfoCommands
                                .slice(InfoCommands.command)
                                .selectAll()
                                .map { it[InfoCommands.command] }
                        call.respond(response)
                    }
                }
                get<InfoCommandsRoute.Get> {
                    newSuspendedTransaction {
                        val response =
                            InfoCommandDetails.find { InfoCommands.command eq it.name }.firstOrNull()
                        if (response != null) {
                            call.respond(
                                InfoCommand(
                                    response.command,
                                    response.description,
                                    response.info,
                                    response.ephemeral,
                                    response.embed,
                                    response.color,
                                    response.enabled,
                                    response.actions
                                )
                            )
                        } else {
                            call.respond(HttpStatusCode.NotFound)
                        }
                    }
                }
            }
        }.start(wait = true)
    }
}

data class UserSession(val token: String) : Principal