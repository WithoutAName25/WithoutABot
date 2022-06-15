package eu.withoutaname.withoutabot.common.api.routes

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("/info-commands")
class InfoCommandsRoute {

    @Serializable
    @Resource("/")
    class Names(val parent: InfoCommandsRoute = InfoCommandsRoute())

    @Serializable
    @Resource("/{name}")
    class Get(val parent: InfoCommandsRoute = InfoCommandsRoute(), val name: String)
}