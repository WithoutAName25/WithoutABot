package withoutabot.pages

import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.router.dom.Link
import withoutabot.layouts.defaultLayout

val Index = FC<Props> {
    defaultLayout {
        div {
            h1 {
                +"Hello World"
            }
            Link {
                to = "/info-commands"
                +"Info Commands"
            }
        }
    }
}