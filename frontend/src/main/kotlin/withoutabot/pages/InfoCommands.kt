package withoutabot.pages

import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.useState
import withoutabot.components.InfoCommandDetail
import withoutabot.components.InfoCommandList
import withoutabot.layouts.defaultLayout

val InfoCommands = FC<Props> {
    defaultLayout {
        var selectedCommand: String? by useState(null)
        div {
            h1 {
                +"Info Commands"
            }
            if (selectedCommand == null) {
                InfoCommandList {
                    onCommandSelected = {
                        selectedCommand = it
                    }
                }
            } else {
                InfoCommandDetail {
                    commandName = selectedCommand!!
                    onShowAllCommands = {
                        selectedCommand = null
                    }
                }
            }
        }
    }
}