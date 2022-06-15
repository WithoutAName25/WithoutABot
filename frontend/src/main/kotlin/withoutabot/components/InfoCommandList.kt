package withoutabot.components

import eu.withoutaname.withoutabot.common.api.routes.InfoCommandsRoute
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import react.useEffectOnce
import react.useState
import withoutabot.client
import withoutabot.mainScope
import withoutabot.styles.Style

external interface InfoCommandListProps : Props {

    var onCommandSelected: (String) -> Unit
}

val InfoCommandList = FC<InfoCommandListProps> { props ->
    var commands: List<String> by useState(listOf())
    useEffectOnce {
        mainScope.launch {
            commands = getCommandNames()
        }
    }
    button {
        +"Refresh"
        onClick = {
            commands = listOf()
            mainScope.launch {
                commands = getCommandNames()
            }
        }
    }
    ul {
        for (command in commands) {
            li {
                className = Style.pointer
                +command
                onClick = {
                    props.onCommandSelected(command)
                }
            }
        }
    }

}

private suspend fun getCommandNames(): List<String> {
    return client.get(InfoCommandsRoute.Names()).body()
}