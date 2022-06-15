package withoutabot.components

import eu.withoutaname.withoutabot.common.api.InfoCommand
import eu.withoutaname.withoutabot.common.api.routes.InfoCommandsRoute
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.textarea
import react.useEffectOnce
import react.useState
import withoutabot.client
import withoutabot.mainScope

external interface InfoCommandDetailProps : Props {

    var commandName: String
    var onShowAllCommands: () -> Unit
}

suspend fun getInfoCommandDetail(commandName: String): InfoCommand {
    return client.get(InfoCommandsRoute.Get(name = commandName)).body()
}

val InfoCommandDetail = FC<InfoCommandDetailProps> { props ->
    var command: InfoCommand? by useState(null)
    useEffectOnce {
        mainScope.launch {
            command = getInfoCommandDetail(props.commandName)
        }
    }
    div {
        button {
            +"all commands"
            onClick = { props.onShowAllCommands() }
        }
        button {
            +"Refresh"
            onClick = {
                mainScope.launch {
                    command = getInfoCommandDetail(props.commandName)
                }
            }
        }
        h2 { +"Command - /${props.commandName}" }
        command?.let { cmd ->
            form {
                label {
                    +"Description: "
                    input {
                        type = InputType.text
                        value = cmd.description
                        onChange = { cmd.description = it.target.value }
                    }
                }
                label {
                    +"Info: "
                    textarea {
                        value = cmd.info
                        onChange = { cmd.info = it.target.value }
                    }
                }
                label {
                    +""
                }
            }
        }
    }
}