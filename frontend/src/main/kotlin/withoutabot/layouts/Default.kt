package withoutabot.layouts

import csstype.*
import emotion.react.Global
import emotion.react.css
import emotion.react.styles
import react.FC
import react.PropsWithChildren
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.footer
import react.dom.html.ReactHTML.header
import react.dom.html.ReactHTML.main
import react.router.dom.Link

val defaultLayout = FC<PropsWithChildren> {
    Global {
        styles {
            ":where(*)" {
                boxSizing = BoxSizing.borderBox
                margin = 0.px
            }
        }
    }
    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            minHeight = 100.vh
            main {
                flexGrow = number(1.0)
            }
        }
        header {
            Link {
                to = "/"
                +"Home"
            }
        }
        main {
            +it.children
        }
        footer {
        }
    }
}